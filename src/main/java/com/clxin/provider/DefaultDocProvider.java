package com.clxin.provider;

import com.clxin.config.JsonApiProperties;
import com.clxin.filter.DocFileFilterHandler;
import com.sun.javadoc.*;
import com.sun.tools.javadoc.Main;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Method;
import java.util.*;


public class DefaultDocProvider implements DocProvider, InitializingBean {


    @Resource
    private JsonApiProperties jsonApiProperties;

    private static DocFileFilterHandler docFileFilterHandler;

    @Resource
    public void setDocFileFilter(DocFileFilterHandler docFileFilterHandler) {
        DefaultDocProvider.docFileFilterHandler = docFileFilterHandler;
    }

    /**
     * Map<全类名,类的注释>
     */
    private static Map<String, String> classDocMap = new HashMap<>();

    /**
     * Map<全类名+方法名, 注释>
     * 如com.a类下有一个b(String a)方法，那么这个方法名就是com.a.b(String)
     */
    private final static Map<String, String> methodDocMap = new HashMap<>();

    /**
     * 保存入参注释
     * Map<一个tag的唯一标识，类名+方法名+入参顺序,Map<注释,字段名>>
     */
    private final static Map<String, AbstractMap.SimpleEntry<String, String>> paramDocMap = new HashMap<>();

    /**
     * 保存returnTag信息
     * Map<方法名,注释>
     */
    private final static Map<String, String> returnTagMap = new HashMap<>();
    /**
     * 参数key，这个参数是入参dto或者返回值vo的参数->注释
     */
    private final static Map<String, String> fieldMap = new HashMap<>();

    /**
     * {@link #afterPropertiesSet}方法中会调用到此方法，并传入RootDoc
     *
     * @param rootDoc 包含了指定的java文件的注释信息的对象
     * @return 不知道
     */
    public static boolean start(RootDoc rootDoc) {
        ClassDoc[] classes = rootDoc.classes();

        classDocMap = new HashMap<>();
        for (ClassDoc classDoc : classes) {
            String className = classDoc.qualifiedName();
            classDocMap.put(className, classDoc.commentText());

            initMethodDocMap(classDoc);
            initFieldDocMap(classDoc);
        }

        return true;
    }

    /**
     * 初始化{@link #methodDocMap}
     *
     * @param classDoc 方法所在类的doc对象
     */
    private static void initMethodDocMap(ClassDoc classDoc) {
        String className = classDoc.qualifiedName();
        MethodDoc[] methods = classDoc.methods();
        for (MethodDoc methodDoc : methods) {
            String methodKey = methodDocKey(className, methodDoc);
            methodDocMap.put(methodKey, methodDoc.commentText());
            initParamDocMap(methodDoc, methodKey);
        }
    }

    private static void initParamDocMap(MethodDoc methodDoc, String methodKey) {
        //这里获取的是方法注释上的如@param这样的注释
        Tag[] tags = methodDoc.tags();
        for (Tag tag : tags) {
            if (tag instanceof ParamTag) {
                ParamTag paramTag = (ParamTag) tag;
                String paramComment = paramTag.parameterComment();
                if (ObjectUtils.isEmpty(paramComment)) {
                    paramComment = "没有注释";
                }
                int i = determineIndex(methodDoc, paramTag.parameterName());
                paramDocMap.put(methodKey + i, new AbstractMap.SimpleEntry<>(paramComment, paramTag.parameterName()));
            } else {
                returnTagMap.put(methodKey, tag.text());
            }
        }
    }

    /**
     * 推断入参名在所在方法中的参数顺序
     *
     * @param methodDoc 入参所在方法
     * @param filedName 入参名
     * @return 顺序（索引，从0开始）
     */
    private static int determineIndex(MethodDoc methodDoc, String filedName) {
        int i = 0;
        for (Parameter parameter : methodDoc.parameters()) {
            if (parameter.name().equals(filedName)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private static void initFieldDocMap(ClassDoc classDoc) {
        for (FieldDoc field : classDoc.fields(false)) {
            fieldMap.put(field.qualifiedName(), field.commentText());
        }
    }

    /**
     * doc的api创建的方法名格式与反射创建的方法名格式不同
     * 所以自定义方法名格式
     *
     * @param methodDoc 方法doc
     * @return 方法名格式为全类名.方法名(参数类名) 如：com.clxin.TestController.get(String)
     */
    private static String methodDocKey(String className, MethodDoc methodDoc) {
        Parameter[] parameters = methodDoc.parameters();
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        if (!ObjectUtils.isEmpty(parameters)) {
            for (Parameter parameter : parameters) {
                int length = builder.length();
                if (length > 1) {
                    builder.append(",");
                }
                builder.append(parameter.type().typeName());
            }
        }
        builder.insert(0, className);
        return builder.append(")").toString();
    }

    /**
     * 获取类的注释
     *
     * @param c 需要获取注释的目标类对象
     * @return 该类的注释
     */
    @Override
    public String getClassComment(Class<?> c) {
        Assert.notNull(c, "获取注释的类不能为null");
        return classDocMap.get(c.getName());
    }

    /**
     * 获取方法注释，需要自己拼接方法名与{@link #methodDocKey}生成的方法名一致
     *
     * @param method 需要获取注释的目标方法对象
     * @return 方法注释
     */
    @Override
    public String getMethodComment(Method method) {
        Assert.notNull(method, "获取注释的方法不能为null");
        String methodKey = methodKey(method);
        return methodDocMap.get(methodKey);
    }


    /**
     * 获取tag注释
     *
     * @param method tag所在的方法
     * @return tag注释
     */
    @Override
    public AbstractMap.SimpleEntry<String, String> getParamComment(Method method, int index) {
        Assert.notNull(method, "获取入参注释的方法不能为null");
        return paramDocMap.get(methodKey(method) + index);
    }

    /**
     * 构建方法key,这里生成的key要与{@link #methodDocKey}中的相同
     *
     * @param method 需要创建key的方法对象
     * @return 该方法生成的key
     */
    private String methodKey(Method method) {
        String className = method.getDeclaringClass().getName();
        java.lang.reflect.Parameter[] parameters = method.getParameters();

        StringBuilder builder = new StringBuilder();
        builder.append("(");
        if (!ObjectUtils.isEmpty(parameters)) {
            for (java.lang.reflect.Parameter parameter : parameters) {
                int length = builder.length();
                if (length > 1) {
                    builder.append(",");
                }
                builder.append(parameter.getType().getSimpleName());
            }
        }
        builder.insert(0, className);
        return builder.append(")").toString();
    }

    /**
     * 初始化doc，这个方法会调用到{@link #start}
     */
    @Override
    public void afterPropertiesSet() {
        List<String> list = new ArrayList<>();
        list.add("-doclet");

        list.add(DefaultDocProvider.class.getName());
        list.add("-encoding");
        list.add("utf-8");
        list.add("-classpath");

        String rootPath = this.getClass().getResource("/static").getPath();

        list.add(rootPath);

        getJavaFile(list, jsonApiProperties.getJavaFilePath());
        String[] strs = list.toArray(new String[]{});

        Main.execute(strs);
    }

    /**
     * 获取目录和子目录下的所有.java文件
     *
     * @param paths 保存文件绝对路径的集合
     * @param path  遍历的路径
     */
    private static void getJavaFile(List<String> paths, String path) {
        Assert.hasText(path, "doc扫描的java文件所在目录路径不能为空，可以使用json-api.java-file-path配置");
        File file = new File(path);
        File[] files = file.listFiles(docFileFilterHandler);
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                getJavaFile(paths, f.getAbsolutePath());
            } else {
                paths.add(f.getAbsolutePath());
            }
        }
    }
}

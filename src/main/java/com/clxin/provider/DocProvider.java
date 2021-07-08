package com.clxin.provider;

import java.lang.reflect.Method;

/**
 * 获取注释
 */
public interface DocProvider {
    String SPRING_JAVA_FILEPATH = "api.javaFilePath";
    String PARAM_TAG_NAME = "@param";
    String RETURN_TAG_NAME = "@return";

    /**
     * 获取类的注释
     *
     * @param c 需要获取注释的目标类对象
     * @return 传入的类的注释
     */
    String getClassComment(Class<?> c);

    /**
     * 获取方法的注释
     *
     * @param method 需要获取注释的目标方法对象
     * @return 传入的方法的注释
     */
    String getMethodComment(Method method);

    /**
     * 获取tag注释，tag就是方法上的@param这类的注释
     * @param method tag所在的方法
     * @param tagName tag的名称
     * @return tag的注释
     */
    String getTagComment(Method method,String tagName);
}

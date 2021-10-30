package com.clxin.provider;

import com.sun.javadoc.ClassDoc;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.AbstractMap;

/**
 * 获取注释
 */
public interface DocProvider {


    /**
     * 获取类的注释
     *
     * @param c 需要获取注释的目标类对象
     * @return 传入的类的注释
     */
    String getClassComment(Class<?> c);

    /**
     * 初始化注释数据
     */
    void initCommentData();

    /**
     * 获取方法的注释
     *
     * @param method 需要获取注释的目标方法对象
     * @return 传入的方法的注释
     */
    String getMethodComment(Method method);

    /**
     * 获取入参注释
     * @param method 入参所在的方法
     * @param index 入参字段所在索引
     * @return 注释
     */
    AbstractMap.SimpleEntry<String, String> getParamComment(Method method, int index);

    ClassDoc getClassDoc(Class<?> clazz);

}

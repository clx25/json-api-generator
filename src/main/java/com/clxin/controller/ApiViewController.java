package com.clxin.controller;

import com.clxin.generator.ApiJsonGenerator;

/**
 * 返回json数据的controller接口
 * @param <T> 组装好的数据类型
 */
public interface ApiViewController<T> {
    /**
     * {@link ApiJsonGenerator}在组装好注释与类，方法，字段的对应关系和结构关系后
     * 通过这个方法传给controller,一般情况下，请求controller就是把这个数据
     * 返回给前端
     * @param apiViewInfo 组装好的api数据
     */
    void setApiViewInfo(T apiViewInfo);
}

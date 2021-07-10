package com.clxin.filter;

/**
 * 参数的过滤器
 */
public interface ParamFilter {

    /**
     * 匹配方法
     * @param paramClass 参数类型
     * @return 如果返回true,表示保留，返回false,表示舍弃
     */
    boolean match(Class<?> paramClass);
}

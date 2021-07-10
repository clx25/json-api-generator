package com.clxin.filter;

/**
 * 过滤apiBean的过滤器
 * 这个apiBean就是controller
 */
public interface ApiBeanFilter {
    /**
     * 需要排除的类
     * @param apiBeanClass controller类
     * @return
     */
    boolean exclude(Class<?> apiBeanClass);
}

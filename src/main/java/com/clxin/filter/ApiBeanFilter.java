package com.clxin.filter;

public interface ApiBeanFilter {

    boolean exclude(Class<?> apiBeanClass);
}

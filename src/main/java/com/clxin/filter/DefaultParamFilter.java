package com.clxin.filter;

import javax.servlet.ServletRequest;

public class DefaultParamFilter implements ParamFilter{

    @Override
    public boolean match(Class<?> c) {
        return !c.isAssignableFrom(ServletRequest.class)&&!c.isAssignableFrom(Class.class);
    }
}

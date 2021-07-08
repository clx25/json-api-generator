package com.clxin.filter;

import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;

public class DefaultApiBeanFilter implements ApiBeanFilter{
    @Override
    public boolean exclude(Class<?> apiBeanClass) {
        return apiBeanClass.equals(BasicErrorController.class);
    }
}

package com.clxin.filter;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;

import java.util.List;

@AllArgsConstructor
public class DefaultApiBeanFilter implements ApiBeanFilter{
    @Override
    public boolean exclude(Class<?> apiBeanClass) {
        return apiBeanClass.equals(BasicErrorController.class);
    }

}

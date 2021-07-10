package com.clxin.filter;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ParamFilterHandler {

    private final List<ParamFilter> paramFilters;


    public boolean accept(Class<?> c) {
        for (ParamFilter paramFilter : paramFilters) {
            if (!paramFilter.match(c)) {
                return false;
            }
        }
        return true;
    }
}

package com.clxin.filter;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * 用于在doc扫描类时
 * 可以继承DocFileFilter并重写accept的方式扩展filter
 */
@Component
@AllArgsConstructor
public class DocFileFilterHandler implements FileFilter {

    /**
     * doc文件过滤接口集合
     */
    private final List<DocFileFilter> docFileFilterList;

    @Override
    public boolean accept(File file) {
        //遍历文件过滤器，判断该file是否过滤
        for (DocFileFilter docFileFilter : docFileFilterList) {
            if (!docFileFilter.match(file)) {
                return false;
            }
        }
        return true;
    }
}

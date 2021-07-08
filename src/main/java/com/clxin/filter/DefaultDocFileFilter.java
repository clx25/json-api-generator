package com.clxin.filter;

import java.io.File;

/**
 * 默认的doc扫描文件过滤器
 */
public class DefaultDocFileFilter  implements DocFileFilter{

    /**
     * 过滤出.java文件，Dto类和Vo类
     *
     * @param file 需要判断的file
     * @return true，不过滤。false,舍弃该file
     */
    @Override
    public boolean match(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String fileName = file.getName();

        return fileName.contains(".java") || fileName.contains("Dto") || fileName.contains("Vo");
    }
}

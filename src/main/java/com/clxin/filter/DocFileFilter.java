package com.clxin.filter;

import java.io.File;

public interface DocFileFilter {

    boolean match(File file);
}

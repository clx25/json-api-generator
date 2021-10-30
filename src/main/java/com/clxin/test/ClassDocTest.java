package com.clxin.test;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javadoc.ClassDocImpl;
import com.sun.tools.javadoc.DocEnv;

public class ClassDocTest extends ClassDocImpl {
    public ClassDocTest(DocEnv docEnv, Symbol.ClassSymbol classSymbol) {
        super(docEnv, classSymbol);
    }
}

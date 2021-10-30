package com.clxin.test;

public class ResultUtil {

    public static  ResultData<?,String>  success() {
        ResultData<Object,String> r = new ResultData<>();
        r.setCode(200);
        r.setMsg("成功");
        return r;
    }

}

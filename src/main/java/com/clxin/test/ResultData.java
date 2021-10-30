package com.clxin.test;

import lombok.Data;

@Data
public class ResultData<K,V> {
    private String msg;
    private int code;
    private K data;
    private V d;
}

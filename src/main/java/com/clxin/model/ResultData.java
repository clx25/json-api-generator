package com.clxin.model;

import lombok.Data;

@Data
public class ResultData<T> {
    private int status;
    private String msg;
    private T data;
}

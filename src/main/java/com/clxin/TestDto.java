package com.clxin;

import lombok.Data;

@Data
public class TestDto<T> {
    private transient String userName;
    private int age;
    private T name;
}

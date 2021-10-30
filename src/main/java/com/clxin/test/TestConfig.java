package com.clxin.test;

import com.clxin.config.ResultExample;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class TestConfig extends ResultExample {


    @Override
    public Map<String, Object> getExample() {
        Map< String,Object> map = new HashMap<>();
        map.put("data", ResultUtil.success());
        return map;
    }
}


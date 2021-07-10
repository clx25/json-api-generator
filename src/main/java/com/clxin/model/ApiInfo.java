package com.clxin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiInfo {

    /**
     * api描述
     */
    @JsonProperty("api描述")
    private String apiDes;

    /**
     * 请求方式
     */
    @JsonProperty("请求方式")
    private String httpMethod;

    /**
     * url
     */
    @JsonProperty("请求地址")
    private String url;

    /**
     * controller的返回对象
     */
    @JsonProperty("返回数据")
    private Object result;

    /**
     * controller的接收对象
     */
    @JsonProperty("接收数据")
    private Map<String, Object> accept = new HashMap<>();
}

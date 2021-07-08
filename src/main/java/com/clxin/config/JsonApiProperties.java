package com.clxin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "json-api")
public class JsonApiProperties {
    /**
     * doc扫描的.java文件所在的文件夹
     */
    private String javaFilePath;
}

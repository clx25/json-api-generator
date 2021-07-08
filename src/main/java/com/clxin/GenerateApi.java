package com.clxin;

import com.clxin.config.GenerateApiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(GenerateApiAutoConfiguration.class)
public class GenerateApi {
    public static void main(String[] args) {
        SpringApplication.run(GenerateApi.class, args);
    }
}


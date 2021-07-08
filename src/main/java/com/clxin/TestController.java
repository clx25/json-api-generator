package com.clxin;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 这是一个测试controller
 */
@Data
@RequestMapping("api")
@RestController
public class TestController {
    /**
     * 这是一个测试字段
     */
    private String t;

    //这也是一个测试字段
    private String t2;

    /**
     * 这是一个测试方法，用来返回对象
     *
     * @param testDto 接收数据
     * @return 返回数据
     */
    @GetMapping("test")
    public TestDto test(TestDto testDto, String s) {
        testDto.setA("666");
        testDto.setB("777");
        return testDto;
    }

    /**
     * 测试String类型入参
     * @param s
     */
    @GetMapping("/")
    public void t2(String s) {

    }

    /**
     * 测试class类型入参
     * @param s
     */
    @GetMapping("/t3")
    public void t3(Class<?> s) {

    }

    /**
     * 测试map类型入参
     * @param map
     */
    @GetMapping("/t4")
    public void t4(Map<String,String> map) {

    }

    @GetMapping("/t5")
    public void t5(int i) {

    }

    @GetMapping("/t6")
    public void t6(String[] ss) {

    }
}

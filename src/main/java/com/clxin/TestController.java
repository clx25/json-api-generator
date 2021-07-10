package com.clxin;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * 测试相关
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
     * 修改用户信息
     *
     * @param testDto 修改用户所需的信息
     * @return 返回数据
     */
    @PutMapping("user")
    public TestDto test(@RequestBody TestDto testDto) {
        return testDto;
    }

    /**
     * 测试String类型入参
     *
     * @param s 用户id
     */
    @GetMapping("/")
    public void t2(String s) {

    }

    /**
     * 测试class类型入参
     *
     * @param s 这是一个类
     */
    @GetMapping("/t3")
    public void t3(Class<?> s) {

    }

    /**
     * 测试map类型入参
     *
     * @param map 这是一个map
     */
    @GetMapping("/t4")
    public void t4(Map<String, String> map) {

    }

    /**
     * 一个参数
     *
     * @param i       数字
     */
    @GetMapping("/t5")
    public int t5(int i) {
        return i;
    }

    @GetMapping("/t6")
    public void t6(String[] ss) {

    }

    /**
     * 测试t7
     *
     * @param s  第一个数据
     * @param s2 第二个数据
     * @return 返回值
     */
    @GetMapping("/t7")
    public String t7(String s, String s2) throws IndexOutOfBoundsException, IOException {
        return "a";
    }
}

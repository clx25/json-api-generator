package com.clxin;

import com.clxin.annotation.ReturnType;
import com.clxin.test.ResultData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

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
     * @param i 数字
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
    public String t7(String s, String s2) throws IndexOutOfBoundsException, JsonProcessingException {
        return new ObjectMapper().writeValueAsString(new ResultData<TestDto,String>());
    }

    /**
     * 一个测试方法
     *
     * @param i       基本类型测试数据
     * @param testDto pojo类型测试数据
     * @return 测试数据
     */
    @PostMapping("/t8/{i}")
    public ResultData<TestDto<Map<String,String>>,TestDto<Integer>> t8(@PathVariable int i,
                                                                       @RequestBody TestDto<String> testDto) {
        return new ResultData<>();
    }

    @GetMapping("/t9")
    public Map<String, String> t9() {
        return new HashMap<>();
    }


    @GetMapping("/t10")
    @ReturnType({List.class, String.class})
    public List<String> t10() {
        return new ArrayList<>();
    }
}

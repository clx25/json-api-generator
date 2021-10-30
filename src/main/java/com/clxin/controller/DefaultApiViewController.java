package com.clxin.controller;

import com.clxin.event.InitCommentEvent;
import com.clxin.model.ApiInfo;
import com.clxin.provider.DocProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class DefaultApiViewController implements ApiViewController<Map<String, List<ApiInfo>>>, ApplicationContextAware {
    private Map<String, List<ApiInfo>> apiViewInfo;

    private ApplicationContext applicationContext;

    @Override
    public void setApiViewInfo(Map<String, List<ApiInfo>> apiViewInfo) {
        this.apiViewInfo = apiViewInfo;
    }


    @GetMapping("/get")
    public String apiView(Model model) {
        ObjectMapper objectMapper = new ObjectMapper();

//        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//
//                gen.writeString("");
//            }
//        });
        System.out.println("测试");
        String json = null;
        try {
            json = objectMapper.writeValueAsString(apiViewInfo);
        } catch (JsonProcessingException e) {
            log.error("序列化api数据失败", e);
        }
        model.addAttribute("apiView", json);
        return "view";
    }


    @GetMapping("/refresh")
    public String refresh(Model model) {
        applicationContext.publishEvent(new InitCommentEvent(this));
        return apiView(model);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

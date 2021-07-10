package com.clxin.controller;

import com.clxin.model.ApiInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
public class DefaultApiViewController implements ApiViewController<Map<String, List<ApiInfo>>> {
    private Map<String, List<ApiInfo>> apiViewInfo;

    @Override
    public void setApiViewInfo(Map<String, List<ApiInfo>> apiViewInfo) {
        this.apiViewInfo = apiViewInfo;
    }


    @GetMapping("/get")
    public String apiView(Model model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString("");
            }
        });
        String json = objectMapper.writeValueAsString(apiViewInfo);
        model.addAttribute("apiView", json);
        return "view";
    }
}

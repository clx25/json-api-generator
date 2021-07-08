package com.clxin;

import com.clxin.generator.ApiJsonGenerator;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TestListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ApiJsonGenerator apiJsonGenerator;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        apiJsonGenerator.generate();
    }


}

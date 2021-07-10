package com.clxin;

import com.clxin.generator.ApiJsonGenerator;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


@AllArgsConstructor
public class ExecuteGeneratorListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ApiJsonGenerator apiJsonGenerator;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        apiJsonGenerator.generate();
    }


}

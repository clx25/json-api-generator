package com.clxin.listener;

import com.clxin.event.InitCommentEvent;
import com.clxin.generator.ApiJsonGenerator;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;

@AllArgsConstructor
public class InitCommentListener implements ApplicationListener<InitCommentEvent> {

    private final ApiJsonGenerator apiJsonGenerator;

    @Override
    public void onApplicationEvent(InitCommentEvent initCommentEvent) {
        apiJsonGenerator.generate();
    }
}

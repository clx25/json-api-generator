package com.clxin.listener;

import com.clxin.event.InitCommentEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class ExecuteGeneratorListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {


    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationContext.publishEvent(new InitCommentEvent(this));

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

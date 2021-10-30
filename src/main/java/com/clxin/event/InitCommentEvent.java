package com.clxin.event;

import com.clxin.controller.ApiViewController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * 初始化注释数据
 */
public class InitCommentEvent extends ApplicationEvent {
    public InitCommentEvent(Object source) {
        super(source);
    }
}

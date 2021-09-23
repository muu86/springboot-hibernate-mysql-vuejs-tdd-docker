package com.mj.taskagile.domain.model.user;

import com.mj.taskagile.domain.model.user.events.UserRegisteredEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserRegisteredEventHandler {
    
    @EventListener(UserRegisteredEvent.class)
    public void handleEvent(UserRegisteredEvent event) {
        log.debug(
            "Handling `{}` registration event",
            event.getUser().getEmailAddress());
    }
}

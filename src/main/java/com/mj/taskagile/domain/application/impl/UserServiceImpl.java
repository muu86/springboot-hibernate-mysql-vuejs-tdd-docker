package com.mj.taskagile.domain.application.impl;

import javax.transaction.Transactional;

import com.mj.taskagile.domain.application.UserService;
import com.mj.taskagile.domain.application.commands.RegistrationCommand;
import com.mj.taskagile.domain.common.event.DomainEventPublisher;
import com.mj.taskagile.domain.common.mail.MailManager;
import com.mj.taskagile.domain.common.mail.MessageVariable;
import com.mj.taskagile.domain.model.user.RegistrationException;
import com.mj.taskagile.domain.model.user.RegistrationManagement;
import com.mj.taskagile.domain.model.user.User;
import com.mj.taskagile.domain.model.user.events.UserRegisteredEvent;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final RegistrationManagement registrationManagement;
    private final DomainEventPublisher domainEventPublisher;
    private final MailManager mailManager;

    @Override
    public void register(RegistrationCommand command) throws RegistrationException {
        Assert.notNull(command, "Parameter `command` must not be null");
        User newUser = registrationManagement.register(
            command.getUsername(),
            command.getEmailAddress(),
            command.getPassword()
        );
        
        sendWelcomMessage(newUser);
        domainEventPublisher.publish(new UserRegisteredEvent(newUser));
    }
    
    private void sendWelcomMessage(User user) {
        mailManager.send(
            user.getEmailAddress(), 
            "Welcome to TaskAgile", 
            "welcome.ftl", 
            MessageVariable.from("user", user));
    }
}

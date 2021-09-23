package com.mj.taskagile.domain.application.impl;

import javax.transaction.Transactional;

import com.mj.taskagile.domain.application.UserService;
import com.mj.taskagile.domain.application.commands.RegistrationCommand;
import com.mj.taskagile.domain.common.event.DomainEventPublisher;
import com.mj.taskagile.domain.common.mail.MailManager;
import com.mj.taskagile.domain.common.mail.MessageVariable;
import com.mj.taskagile.domain.model.user.RegistrationException;
import com.mj.taskagile.domain.model.user.User;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private RegistrationManagement registrationManagement;
    private DomainEventPublisher domainEventPublisher;
    private MailManager mailManager;

    public UserServiceImpl(RegistrationManagement registrationManagement, DomainEventPublisher domainEventPublisher,
            MailManager mailManager) {
        this.registrationManagement = registrationManagement;
        this.domainEventPublisher = domainEventPublisher;
        this.mailManager = mailManager;
    }

    @Override
    public void register(RegistrationCommand command) throws RegistrationException {
        Assert.notNull(command, "Parameter `command` must not be null");
        User newUser = registrationManagement.register(
            command.getUsername(),
            command.getEmailAddress(),
            command.getPassword()
        );
        
        sendWelcomMessage(newUser);
    }
    
    private void sendWelcomMessage(User user) {
        mailManager.send(
            user.getEmailAddress(), 
            "Welcome to TaskAgile", 
            "welcome.ftl", 
            MessageVariable.from("user", user));
    }
}

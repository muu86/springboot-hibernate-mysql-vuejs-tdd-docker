package com.mj.taskagile.domain.application.impl;

import javax.transaction.Transactional;

import com.mj.taskagile.domain.application.UserService;
import com.mj.taskagile.domain.application.commands.RegistrationCommand;
import com.mj.taskagile.domain.common.event.DomainEventPublisher;
import com.mj.taskagile.domain.common.mail.MailManager;
import com.mj.taskagile.domain.common.mail.MessageVariable;
import com.mj.taskagile.domain.model.user.RegistrationException;
import com.mj.taskagile.domain.model.user.RegistrationManagement;
import com.mj.taskagile.domain.model.user.SimpleUser;
import com.mj.taskagile.domain.model.user.User;
import com.mj.taskagile.domain.model.user.UserRepository;
import com.mj.taskagile.domain.model.user.events.UserRegisteredEvent;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final RegistrationManagement registrationManagement;
    private final DomainEventPublisher domainEventPublisher;
    private final MailManager mailManager;
    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new UsernameNotFoundException("No user found");
        }
        
        User user;
        if (username.contains("@")) {
            user = userRepository.findByEmailAddress(username);
        } else {
            user = userRepository.findByUsername(username);
        }

        if (user == null) {
            throw new UsernameNotFoundException("No user found by `" + username + "`");
        }

        return new SimpleUser(user);
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

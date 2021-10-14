package com.mj.taskagile.domain.application.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mj.taskagile.domain.application.commands.RegistrationCommand;
import com.mj.taskagile.domain.common.event.DomainEventPublisher;
import com.mj.taskagile.domain.common.mail.MailManager;
import com.mj.taskagile.domain.common.mail.MessageVariable;
import com.mj.taskagile.domain.model.user.EmailAddressExistsException;
import com.mj.taskagile.domain.model.user.RegistrationException;
import com.mj.taskagile.domain.model.user.RegistrationManagement;
import com.mj.taskagile.domain.model.user.User;
import com.mj.taskagile.domain.model.user.UserRepository;
import com.mj.taskagile.domain.model.user.UsernameExistsException;
import com.mj.taskagile.domain.model.user.events.UserRegisteredEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceImplTest {

    private RegistrationManagement registrationManagementMock;
    private DomainEventPublisher domainEventPublisherMock;
    private MailManager mailManagerMock;
    private UserRepository userRepository;
    private UserServiceImpl instance;

    @BeforeEach
    public void setup() {
        registrationManagementMock = mock(RegistrationManagement.class);
        domainEventPublisherMock = mock(DomainEventPublisher.class);
        mailManagerMock = mock(MailManager.class);
        userRepository = mock(UserRepository.class);
        
        instance = new UserServiceImpl(
            registrationManagementMock,
            domainEventPublisherMock,
            mailManagerMock,
            userRepository
        );
    }

    @Test
    public void register_nullCommand_shouldFail() throws RegistrationException {
        assertThrows(IllegalArgumentException.class, () -> {
            instance.register(null);
        });
    }

    @Test
    public void register_existingUsername_shouldFail() throws RegistrationException {
        String username = "existing";
        String emailAddress = "sunny@taskagile.com";
        String password = "password";

        doThrow(UsernameExistsException.class)
            .when(registrationManagementMock)
            .register(username, emailAddress, password);

        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
        assertThrows(RegistrationException.class, () -> {
            instance.register(command);
        });
    }

    @Test
    public void register_existingEmailAddress_shouldFail() throws RegistrationException {
        String username = "mjkkimg";
        String emailAddress = "exist@taskagile.com";
        String password = "password";

        doThrow(EmailAddressExistsException.class)
            .when(registrationManagementMock)
            .register(username, emailAddress, password);

        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
        assertThrows(RegistrationException.class, () -> {
            instance.register(command);
        });
    }

    @Test
    public void register_validCommand_shouldSucceed() throws RegistrationException {
        String username = "mjking";
        String emailAddress = "mjking@taskagile.com";
        String password = "password";
        User newUser = User.create(username, emailAddress, password);
        
        when(registrationManagementMock.register(username, emailAddress, password))
            .thenReturn(newUser);
        RegistrationCommand command = new RegistrationCommand(username, emailAddress, password);
        instance.register(command);

        verify(mailManagerMock).send(
            emailAddress,
            "Welcome to TaskAgile",
            "welcome.ftl",
            MessageVariable.from("user", newUser)
        );
        verify(domainEventPublisherMock).publish(new UserRegisteredEvent(newUser));
    }
}

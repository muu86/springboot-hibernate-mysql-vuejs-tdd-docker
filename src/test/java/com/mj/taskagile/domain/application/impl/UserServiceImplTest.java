package com.mj.taskagile.domain.application.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.mj.taskagile.domain.application.commands.RegistrationCommand;
import com.mj.taskagile.domain.model.EmailAddressExistsException;
import com.mj.taskagile.domain.model.RegistrationException;
import com.mj.taskagile.domain.model.UsernameExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceImplTest {

    private RegistrationManagement registrationManagementMock;
    // private DomainEventPublisher domainEventPublisherMock;
    // private MailManager mailManagerMock;
    private UserServiceImpl instance;

    @BeforeEach
    public void setup() {
        registrationManagementMock = mock(RegistrationManagement.class);
        // eventPublisherMock = mock(DomainEventPublisher.class);
        // mailManagerMock = mock(MailManager.class);
        instance = new UserServiceImpl(
            registrationManagementMock
            // eventPublisherMock,
            // mailManagerMock
        );
    }

    @Test
    public void register_nullCommand_shouldFail() throws RegistrationException {
        assertThrows(IllegalArgumentException.class, () -> {
            instance.register(null);
        });
    }

    @Test
    public void register_existingUsername_shouldFail() {
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
    public void register_existingEmailAddress_shouldFail() {
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
            "welcom.ftl",
            MessageVariable.from("user", newUser)
        );
        verify(domainEventPublisherMock).publish(new UserRegisteredEvent(newUser));
    } 

    
}

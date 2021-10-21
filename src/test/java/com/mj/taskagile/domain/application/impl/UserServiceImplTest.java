package com.mj.taskagile.domain.application.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mj.taskagile.domain.application.commands.RegistrationCommand;
import com.mj.taskagile.domain.common.event.DomainEventPublisher;
import com.mj.taskagile.domain.common.mail.MailManager;
import com.mj.taskagile.domain.common.mail.MessageVariable;
import com.mj.taskagile.domain.model.user.EmailAddressExistsException;
import com.mj.taskagile.domain.model.user.RegistrationException;
import com.mj.taskagile.domain.model.user.RegistrationManagement;
import com.mj.taskagile.domain.model.user.SimpleUser;
import com.mj.taskagile.domain.model.user.User;
import com.mj.taskagile.domain.model.user.UserRepository;
import com.mj.taskagile.domain.model.user.UsernameExistsException;
import com.mj.taskagile.domain.model.user.events.UserRegisteredEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    public void loadUserByUsername_emptyUsername_shouldFail() {
        // assertThrows(UsernameNotFoundException.class, () -> {
        //     instance.loadUserByUsername("");
        // });

        Exception exception = null;
        try {
          instance.loadUserByUsername("");
        } catch (Exception e) {
          exception = e;
        }
        assertNotNull(exception);
        assertTrue(exception instanceof UsernameNotFoundException);
        verify(userRepository, never()).findByUsername("");
        verify(userRepository, never()).findByEmailAddress("");
    }

    @Test
    public void loadUserByUsername_notExistUsername_shoudFail() {
        String notExistUsername = "NotExistUsername";
        when(userRepository.findByUsername(notExistUsername)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            instance.loadUserByUsername(notExistUsername);
        });
        verify(userRepository).findByUsername(notExistUsername);
        verify(userRepository, never()).findByEmailAddress(notExistUsername);
    }

    @Test
    public void loadUserByUsername_existUsername_shouldSucceed() throws IllegalAccessException {
        String existUsername = "ExistUsername";
        // User foundUser = User.create(existUsername, "user@taskagile.com", "EncryptedPassword!");
        // foundUser.updateName("Test", "User");

        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn(existUsername);
        when(mockUser.getPassword()).thenReturn("EncryptedPassword!");
        when(mockUser.getId()).thenReturn(1L);

        // FieldUtils.writeField(mockUser, "id", 1L, true);
        when(userRepository.findByUsername(existUsername)).thenReturn(mockUser);

        Exception exception = null;
        UserDetails userDetails = null;
        try {
            userDetails = instance.loadUserByUsername(existUsername);
        } catch (Exception e) {
            exception = e;
        }
        assertNull(exception);
        verify(userRepository).findByUsername(existUsername);
        verify(userRepository, never()).findByEmailAddress(existUsername);
        assertNotNull(userDetails);
        assertEquals(existUsername, userDetails.getUsername());
        assertTrue(userDetails instanceof SimpleUser);
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

package com.mj.taskagile.domain.model.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mj.taskagile.domain.common.security.PasswordEncryptor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class RegistrationManagementTest {

    private UserRepository repositoryMock;
    private PasswordEncryptor passwordEncryptorMock;
    private RegistrationManagement instance;

    @BeforeEach
    public void setUp() {
        repositoryMock = mock(UserRepository.class);
        passwordEncryptorMock = mock(PasswordEncryptor.class);
        instance = new RegistrationManagement(repositoryMock, passwordEncryptorMock);
        System.out.println(repositoryMock);
        System.out.println(passwordEncryptorMock);
    }

    @Test
    public void register_existedUsername_shouldFail() throws RegistrationException {
        String username = "existUsername";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";

        when(repositoryMock.findByUsername(username)).thenReturn(new User());

        // doThrow(UsernameExistsException.class)
        //     .when(instance)
        //     .register(username, emailAddress, password);

        assertThrows(UsernameExistsException.class, () -> {
            instance.register(username, emailAddress, password);
        });
    }

    @Test
    public void register_existedEmailAddress_shouldFail() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "exist@taskagile.com";
        String password = "MyPassword!";

        when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(new User());

        assertThrows(EmailAddressExistsException.class, () -> {
            instance.register(username, emailAddress, password);
        });
    }

    @Test
    public void register_uppercaseEmailAddress_shouldSucceedAndBecomeLowercase() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "Sunny@TaskAgile.com";
        String password = "MyPassword!";

        instance.register(username, emailAddress, password);

        User userToSave = User.create(username, emailAddress.toLowerCase(), password);
        verify(repositoryMock).save(userToSave);
    }

    @Test
    public void register_newUser_shouldSucceed() throws RegistrationException {
        String username = "sunny";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";
        String encryptedPassword = "EncryptedPassword";
        User newUser = User.create(username, emailAddress, password);

        when(repositoryMock.findByUsername(username)).thenReturn(null);
        when(repositoryMock.findByEmailAddress(emailAddress)).thenReturn(null);
        doNothing().when(repositoryMock).save(newUser);
        when(passwordEncryptorMock.encrypt(password)).thenReturn("EncryptedPassword");

        User savedUser = instance.register(username, emailAddress, password);
        InOrder inOrder = inOrder(repositoryMock);
        inOrder.verify(repositoryMock).findByUsername(username);
        inOrder.verify(repositoryMock).findByEmailAddress(emailAddress);
        inOrder.verify(repositoryMock).save(newUser);
        verify(passwordEncryptorMock).encrypt(password);
        assertEquals(encryptedPassword, savedUser.getPassword(), "Saved user's password should be encrypted");
    }
}

package com.mj.taskagile.domain.model.user;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationManagementTest {

    private UserRepository repositoryMock;
    private PasswordEncryptor passwordEncryptorMock;
    private RegistrationManagement instance;

    @BeforeEach
    public void setUp() {
        repositoryMock = mock(UserRepository.class);
        passwordEncryptorMock = mock(PasswordEncryptor.class);
        instance = new RegistrationManagement(repositoryMock, passwordEncryptorMock);
    }

    @Test
    public void register_existedUsername_shouldFail() throws RegistrationException {
        String username = "existUsername";
        String emailAddress = "sunny@taskagile.com";
        String password = "MyPassword!";

        when(repositoryMock.findByUsername(username)).thenReturn(new User());

        doThrow(UsernameExistsException.class)
            .when(instance)
            .register(username, emailAddress, password);
            
    }
}

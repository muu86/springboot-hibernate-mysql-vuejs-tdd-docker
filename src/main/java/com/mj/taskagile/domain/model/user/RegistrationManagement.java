package com.mj.taskagile.domain.model.user;

import com.mj.taskagile.domain.common.security.PasswordEncryptor;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RegistrationManagement {
    
    private final UserRepository repository;
    private final PasswordEncryptor  passwordEncryptor;

    public User register(String username, String emailAddress, String password) throws RegistrationException {
        User existingUser = repository.findByUsername(username);
        if (existingUser != null) {
            throw new UsernameExistsException();
        }

        existingUser = repository.findByEmailAddress(emailAddress);
        if (existingUser != null) {
            throw new EmailAddressExistsException();
        }

        String encryptedPassword = passwordEncryptor.encrypt(password);
        User newUser = User.create(username, emailAddress.toLowerCase(), encryptedPassword);
        repository.save(newUser);
        return newUser;
    }
}

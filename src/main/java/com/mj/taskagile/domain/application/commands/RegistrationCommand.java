package com.mj.taskagile.domain.application.commands;

import org.springframework.util.Assert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter             // setter X
@EqualsAndHashCode  // mockito 비교를 위해
@ToString
public class RegistrationCommand {
    
    private String username;
    private String emailAddress;
    private String password;

    public RegistrationCommand(String username, String emailAddress, String password) {
        Assert.hasText(username, "Parameter `username` must not be empty");
        Assert.hasText(emailAddress, "Parameter `emailAddress` must not be empty");
        Assert.hasText(password, "Parameter `password` must not be empty");
    
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }

}

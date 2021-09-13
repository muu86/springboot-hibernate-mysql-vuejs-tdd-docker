package com.mj.taskagile.domain.application.impl;

import com.mj.taskagile.domain.application.UserService;
import com.mj.taskagile.domain.application.commands.RegistrationCommand;
import com.mj.taskagile.domain.model.RegistrationException;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public void register(RegistrationCommand command) throws RegistrationException {
        System.out.println(command.getUsername() + " 이 가입 신청");        
    }
    
}

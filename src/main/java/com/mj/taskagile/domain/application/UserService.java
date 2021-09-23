package com.mj.taskagile.domain.application;

import com.mj.taskagile.domain.application.commands.RegistrationCommand;
import com.mj.taskagile.domain.model.user.RegistrationException;

public interface UserService {
    
    void register(RegistrationCommand command) throws RegistrationException;
}

package com.mj.taskagile.web.apis;

import javax.validation.Valid;

import com.mj.taskagile.domain.application.UserService;
import com.mj.taskagile.domain.model.EmailAddressExistsException;
import com.mj.taskagile.domain.model.RegistrationException;
import com.mj.taskagile.domain.model.UsernameExistsException;
import com.mj.taskagile.web.payload.RegistrationPayload;
import com.mj.taskagile.web.results.ApiResult;
import com.mj.taskagile.web.results.Result;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class RegistrationApiController {

    private final UserService userService;

    @PostMapping("/api/registrations")
    public ResponseEntity<ApiResult> register(
        @Valid @RequestBody RegistrationPayload payload) {
        
        try {
            userService.register(payload.toCommand());
            return Result.created();
        } catch (RegistrationException e) {
            String errorMessage = "Registration failed";
            if (e instanceof UsernameExistsException) {
                errorMessage = "등록된 이름입니다";
            } else if (e instanceof EmailAddressExistsException) {
                errorMessage = "등록된 이메일입니다";
            }
            return Result.failure(errorMessage);
        }

    }
    
}

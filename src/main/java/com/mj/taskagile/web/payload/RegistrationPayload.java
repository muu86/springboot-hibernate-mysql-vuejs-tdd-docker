package com.mj.taskagile.web.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mj.taskagile.domain.application.commands.RegistrationCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationPayload {
    
    @Size(min = 2, max = 50, message = "이름은 2 ~ 50 글자까지 가능합니다")
    @NotNull
    private String username;

    @Email(message = "유효한 이메일이어야 합니다")
    @Size(max = 100, message = "이메일은 100글자까지 가능합니다")
    @NotNull
    private String emailAddress;

    @Size(min = 6, max = 30, message = "비밀번호는 6 ~ 30 글자까지 가능합니다")
    @NotNull
    private String password;

    // Service 에 전달
    public RegistrationCommand toCommand() {
        return new RegistrationCommand(this.username, this.emailAddress, this.password);
    }
}

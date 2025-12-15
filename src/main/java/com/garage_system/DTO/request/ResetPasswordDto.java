package com.garage_system.DTO.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {

    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirmedNewPassword;

}

package com.garage_system.dto.request ;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerificationDto {
    @NotBlank(message = "please insert the verification code") 
    String verificationCode ;
    
    @Email(message = "email format is not valid")
    String email ;


}
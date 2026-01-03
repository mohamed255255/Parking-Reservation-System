package com.parking_reservation_system.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parking_reservation_system.validator.PhoneValidation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserDto
 {
    private int id;
    
    @Email(message = "Email format is invalid")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password cant be filled with whitespace / null / empty ")
    @Size(min = 6, max = 20, message = "passwords should be greater than 6 and less than 20")
    private String password;

    public LoginUserDto() {}

    public LoginUserDto(int id, String name, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}

package com.garage_system.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.garage_system.DTO.request.RegisterUserDto;
import com.garage_system.Model.User;

public class UserMapper {
    public static RegisterUserDto toDto(User user) {
        return new RegisterUserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public static User toUser(RegisterUserDto dto, PasswordEncoder encoder) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        return user;
    }
}
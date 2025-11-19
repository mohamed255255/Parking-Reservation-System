package com.garage_system.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.garage_system.DTO.request.UserDto;
import com.garage_system.Model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public static User toUser(UserDto dto, PasswordEncoder encoder) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        return user;
    }
}
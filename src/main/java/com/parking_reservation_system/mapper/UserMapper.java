package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.RegisterUserDto;
import com.parking_reservation_system.dto.response.RegisterUserResponseDto;
import com.parking_reservation_system.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    public static RegisterUserDto toDto(User user) {
        if (user == null) return null;

        return new RegisterUserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getRoles());
    }

    // Request DTO -> Entity
    public static User toUser(RegisterUserDto dto, PasswordEncoder encoder) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.id());
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));
        user.setPhone(dto.phone());
        user.setRoles(dto.roles());
        return user;
    }

    public static RegisterUserResponseDto toResponseDto(User user) {
        if (user == null) return null;

        return new RegisterUserResponseDto(
                user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRoles());
    }
}

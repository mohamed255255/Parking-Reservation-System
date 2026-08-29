package com.parking_reservation_system.mapper;

import com.parking_reservation_system.dto.request.RegisterUserRequest;
import com.parking_reservation_system.dto.response.RegisterUserResponse;
import com.parking_reservation_system.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    private UserMapper() {}

    public static RegisterUserRequest toDto(User user) {
        if (user == null) {
            throw new IllegalArgumentException("the argument passed to this method is null");
        }

        return new RegisterUserRequest(
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getRoles());
    }

    public static User toUser(RegisterUserRequest dto, PasswordEncoder encoder) {
        if (dto == null || encoder == null) {
            throw new IllegalArgumentException("one of the argument passed to this method is null");
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));
        user.setPhone(dto.phone());
        user.setVerified(false);
        user.setRoles(dto.roles());
        return user;
    }

    public static RegisterUserResponse toResponseDto(User user) {
        if (user == null) {
            throw new IllegalArgumentException("the argument passed to this method is null");
        }
        return new RegisterUserResponse(
                user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRoles());
    }
}

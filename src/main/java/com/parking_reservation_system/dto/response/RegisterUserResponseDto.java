package com.parking_reservation_system.dto.response;

import java.util.List;
import com.parking_reservation_system.model.Role;

public record RegisterUserResponseDto(
        int id,
        String name,
        String email,
        String phone,
        List<Role> roles

) {}

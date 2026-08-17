package com.parking_reservation_system.dto.response;

import com.parking_reservation_system.model.Role;
import java.util.List;

public record RegisterUserResponseDto(
        int id, String name, String email, String phone, List<Role> roles) {}

package com.parking_reservation_system.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.parking_reservation_system.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
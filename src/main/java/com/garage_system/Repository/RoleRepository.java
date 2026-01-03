package com.garage_system.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.garage_system.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
package com.garage_system.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.garage_system.Model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
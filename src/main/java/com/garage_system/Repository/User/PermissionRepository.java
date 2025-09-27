package com.garage_system.Repository.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.garage_system.Model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
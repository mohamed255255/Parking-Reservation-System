package com.garage_system.Repository;
import  com.garage_system.Model.Admin ;
import  org.springframework.data.jpa.repository.JpaRepository;
import  org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Integer> {
    
}

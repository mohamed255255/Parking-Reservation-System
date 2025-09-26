package com.garage_system.Repository.Admin;
import  org.springframework.data.jpa.repository.JpaRepository ;
import  org.springframework.stereotype.Repository;

import  com.garage_system.Model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Integer> {
    
}

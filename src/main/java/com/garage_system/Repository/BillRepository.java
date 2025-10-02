package com.garage_system.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.garage_system.Model.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    
    @Query("select b from Bill b")
    List<Bill> getAllBills();
} 
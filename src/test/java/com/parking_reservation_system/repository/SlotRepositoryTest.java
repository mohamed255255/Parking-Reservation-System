package com.parking_reservation_system.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;

@DataJpaTest
public class SlotRepositoryTest {

    @Autowired
    SlotRepository SlotRepositoryInMemory ; /// the actual repo will call H2 DB

    /// check lock is working properly
    /// Transactions actually commit or roll back
    /// eager loading of slot with vehicle and user


    /// act
  
    /// assert

    
}

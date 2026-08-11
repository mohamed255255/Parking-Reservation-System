package com.parking_reservation_system.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SlotRepositoryTest {

    @Autowired SlotRepository SlotRepositoryInMemory; // / the actual repo will call H2 DB

    /// check lock is working properly
    /// Transactions actually commit or roll back
    /// eager loading of slot with vehicle and user

    /// act

    /// assert

}

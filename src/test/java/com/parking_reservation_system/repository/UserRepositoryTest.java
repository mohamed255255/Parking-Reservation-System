package com.parking_reservation_system.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.parking_reservation_system.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    public void checkIfEmailExists() {

        /// given
        User user = new User(null, "mido", "test@gmail.com", "123", "01001111111");
        userRepository.save(user);
        /// when
        boolean expected = userRepository.existsByEmail("test@gmail.com");
        /// then
        assertThat(expected).isTrue();
    }
}

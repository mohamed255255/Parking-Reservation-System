package com.parking_reservation_system.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.parking_reservation_system.model.User;

public class UserTestFactory {
    public static User createRandomUser() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        User user = new User();
        user.setId(1);
        user.setName("Test User " + uuid);
        user.setEmail("user_" + uuid + "@example.com");
        user.setPassword("$2a$10$e8.mockedHashedPasswordValue123456789"); // Mock BCrypt hash
        user.setPhone("+1555" + String.format("%07d", random.nextInt(10_000_000)));
        return user;
    }
}

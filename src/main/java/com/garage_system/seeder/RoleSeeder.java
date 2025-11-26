package com.garage_system.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class RoleSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if any rows exist in roles table
        Long count = ((Number) em.createNativeQuery("SELECT COUNT(*) FROM roles")
                                 .getSingleResult()).longValue();

        if (count == 0) {
            // Insert default roles
            em.createNativeQuery("INSERT INTO roles (id, name) VALUES (1, 'USER')").executeUpdate();
            em.createNativeQuery("INSERT INTO roles (id, name) VALUES (2, 'ADMIN')").executeUpdate();
            System.out.println("Roles seeded successfully!");
        }
    }
}

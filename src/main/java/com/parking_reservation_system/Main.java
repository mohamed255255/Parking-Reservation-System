package com.parking_reservation_system;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // listens to entities that uses @CreatedDate @LastModifiedDate 
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}

package com.parking_reservation_system.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@Entity
@Table(name = "password_reset_tokens")
@NoArgsConstructor

@EntityListeners(AuditingEntityListener.class)
public class PasswordResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "token" , unique = true , nullable = false) 
    private String token ;

    @OneToOne
    @JoinColumn(name="user_id" , nullable= false)
    private User user ;

    @Column(nullable = false )
    private LocalDateTime expiryDate ;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;
   
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDate updatedAt;

}

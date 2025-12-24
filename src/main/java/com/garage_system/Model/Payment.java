package com.garage_system.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Setter
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @Column(unique = true)
    private String idempotency_key ;

    @NotNull
    private double amount ;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Method method;

    public enum Status {
        ACCEPTED,
        FAILED,
        REFUNDED
    }

    public enum Method {
        CARD,
        E_WALLET
    }

    @CreatedDate
    @NotNull
    private LocalDateTime createdAt;

    @LastModifiedDate
    @NotNull
    private LocalDateTime updatedAt;
 
    @ManyToOne
    @NotNull
    private Reservation reservation ;
}

package com.parking_reservation_system.repository;

import com.parking_reservation_system.model.IdempotencyKey;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IdempotencyKey> findById(UUID key);

    void deleteByCreatedAtBefore(LocalDateTime time);
}

package com.parking_reservation_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parking_reservation_system.model.IdempotencyKey;
import com.parking_reservation_system.repository.IdempotencyKeyRepository;
import com.parking_reservation_system.service.payment.PaymentService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock IdempotencyKeyRepository idempotencyKeyRepository;

    @InjectMocks PaymentService paymentService;

    @Test
    void getIdempotencyKey_whenStatusCompleted_returnsRecordUnchanged() {
        UUID uuid = UUID.randomUUID();
        IdempotencyKey completedKey = new IdempotencyKey();
        completedKey.setIdempotency_key(uuid);
        completedKey.setStatus("COMPLETED");
        completedKey.setPayload("{\"data\":\"test\"}");
        completedKey.setCreatedAt(LocalDateTime.now().minusSeconds(30));

        when(idempotencyKeyRepository.findById(uuid)).thenReturn(Optional.of(completedKey));

        Optional<IdempotencyKey> result = paymentService.getIdempotencyKey(uuid);

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo("COMPLETED");
        verify(idempotencyKeyRepository, never()).save(any());
    }

    @Test
    void getIdempotencyKey_whenProcessingAndNotZombie_throwsConflict() {
        UUID uuid = UUID.randomUUID();
        IdempotencyKey key = new IdempotencyKey();
        key.setIdempotency_key(uuid);
        key.setStatus("PROCESSING");
        key.setPayload("{\"data\":\"test\"}");
        key.setResponse_body(null);
        key.setResponse_code(0);
        key.setCreatedAt(LocalDateTime.now().minusSeconds(30)); // 30 sec = NOT zombie

        when(idempotencyKeyRepository.findById(uuid)).thenReturn(Optional.of(key));

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> paymentService.getIdempotencyKey(uuid));

        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(exception.getReason()).isEqualTo("Payment already processing");
        verify(idempotencyKeyRepository, never()).save(any());
    }

    @Test
    void getIdempotencyKey_whenProcessingAndZombie_resetsAndSaves() {
        UUID uuid = UUID.randomUUID();
        IdempotencyKey zombieKey = new IdempotencyKey();
        zombieKey.setIdempotency_key(uuid);
        zombieKey.setStatus("PROCESSING");
        zombieKey.setPayload("{\"data\":\"test\"}");
        zombieKey.setResponse_body(null);
        zombieKey.setResponse_code(0);
        LocalDateTime oldTime = LocalDateTime.now().minusSeconds(61); // 61 sec = zombie
        zombieKey.setCreatedAt(oldTime);

        when(idempotencyKeyRepository.findById(uuid)).thenReturn(Optional.of(zombieKey));

        when(idempotencyKeyRepository.save(any(IdempotencyKey.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<IdempotencyKey> result = paymentService.getIdempotencyKey(uuid);

        assertThat(result).isPresent();

        ArgumentCaptor<IdempotencyKey> captor = ArgumentCaptor.forClass(IdempotencyKey.class);
        verify(idempotencyKeyRepository).save(captor.capture());

        IdempotencyKey savedKey = captor.getValue();
        assertThat(savedKey.getStatus()).isEqualTo("PROCESSING");
        assertThat(savedKey.getCreatedAt()).isAfter(oldTime); // Timestamp was reset
    }

    @Test
    void getIdempotencyKey_whenStatusNotCompletedOrProcessing_resetsAndSaves() {
        UUID uuid = UUID.randomUUID();

        IdempotencyKey failedKey = new IdempotencyKey();
        failedKey.setIdempotency_key(uuid);
        failedKey.setStatus("FAILED");
        failedKey.setPayload("{\"data\":\"test\"}");
        failedKey.setCreatedAt(LocalDateTime.now().minusSeconds(30));

        when(idempotencyKeyRepository.findById(uuid)).thenReturn(Optional.of(failedKey));

        when(idempotencyKeyRepository.save(any(IdempotencyKey.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<IdempotencyKey> result = paymentService.getIdempotencyKey(uuid);

        assertThat(result).isPresent();
        verify(idempotencyKeyRepository).save(any(IdempotencyKey.class));
    }
}

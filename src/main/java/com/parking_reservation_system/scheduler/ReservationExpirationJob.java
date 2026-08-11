package com.parking_reservation_system.scheduler;

import com.parking_reservation_system.repository.ReservationRepository;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationExpirationJob {

    private final ReservationRepository reservationRepository;

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void expirePendingReservations() {
        /// Transactional steps
        /// indexed scan on created_at column
        ///  if the status is pending and created_at is before now-30min
        ///  we update it to expired
        reservationRepository.expirePendingReservations();
    }
}

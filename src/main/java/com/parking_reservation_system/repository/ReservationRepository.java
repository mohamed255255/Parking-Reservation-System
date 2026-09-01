package com.parking_reservation_system.repository;

import com.parking_reservation_system.model.Reservation;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository
        extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {

    @Query(
            "SELECT r FROM Reservation r WHERE r.garage.id = :garageId "
                    + "AND r.slot.id = :slotId AND r.status = :status")
    Optional<Reservation> findActiveReservation(int garageId, int slotId, Reservation.Status status);

    @Transactional
    @Modifying
    @Query(
            value =
                    "UPDATE Reservation "
                            + "SET status = 'EXPIRED' "
                            + "WHERE status = 'PENDING' "
                            + "AND created_at <= CURRENT_TIMESTAMP - INTERVAL '30 minutes'",
            nativeQuery = true)
    int expirePendingReservations();

    @Query(
            """
       SELECT r
       FROM Reservation r
       JOIN r.slot s
       WHERE r.endingTime <= CURRENT_TIMESTAMP
       AND r.endingTime >= CURRENT_DATE
    """)
    List<Reservation> findEndedReservationsToday();
}

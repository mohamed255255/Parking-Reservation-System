package com.parking_reservation_system.specification;

import org.springframework.data.jpa.domain.Specification;
import com.parking_reservation_system.model.Reservation;
import java.time.LocalDateTime;

public class ReservationSpecs {

    public static Specification<Reservation> hasUser(Integer userId) {
        return (root, query, builder) ->
                userId == null ? null : builder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Reservation> hasSlotId(Integer slotId) {
        return (root, query, builder) ->
                slotId == null ? null : builder.equal(root.get("slot").get("id"), slotId);
    }

    public static Specification<Reservation> hasGarageId(Integer garageId) {
        return (root, query, builder) ->
                garageId == null ? null : builder.equal(root.get("garage").get("id"), garageId);
    }

    public static Specification<Reservation> hasStatus(Reservation.Status status) {
        return (root, query, builder) ->
                status == null ? null : builder.equal(root.get("status"), status);
    }

    public static Specification<Reservation> betweenTime(LocalDateTime start, LocalDateTime end) {
        return (root, query, builder) -> {
            if (start == null && end == null) return null;
            if (start != null && end != null)
                return builder.between(root.get("startingTime"), start, end);
            if (start != null)
                return builder.greaterThanOrEqualTo(root.get("startingTime"), start);
            return builder.lessThanOrEqualTo(root.get("endingTime"), end);
        };
    }
}

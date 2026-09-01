package com.parking_reservation_system.service;

import com.parking_reservation_system.dto.request.ReservationUserRequest;
import com.parking_reservation_system.dto.response.ReservationResponse;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.ReservationMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.GarageRepository;
import com.parking_reservation_system.repository.ReservationRepository;
import com.parking_reservation_system.repository.SlotRepository;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.security.CustomUserDetails;
import com.parking_reservation_system.specification.ReservationSpecs;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory
;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final GarageRepository garageRepository;

    @Value("${parking.price.hourly}")
    private int hourlyPrice;

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final QRCodeService qrCodeService;
    private final VehicleRepository vehicleRepository;
    private final SlotService slotService;

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    public double calculateFees(Reservation reservation) {
        LocalDateTime start = reservation.getStartingTime();
        LocalDateTime end = reservation.getEndingTime();
        long minutes = Duration.between(start, end).toMinutes();
        double hours = Math.ceil((minutes / 60.0) * 100) / 100;
        return hourlyPrice * hours;
    }

    @Transactional
    public Map<String , Object> createReservation(
            CustomUserDetails userDetails,
            ReservationUserRequest ReservationUserRequest) {

            Slot requiredSlot =
                    slotRepository
                            .findByIdWithALock(ReservationUserRequest.slotId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException(
                                                    "this slot is not found in the slots table"));

            Vehicle choosenVehicle =
                    vehicleRepository
                            .findById(ReservationUserRequest.vehicleId())
                            .orElseThrow(
                                 () ->
                                  new ResourceNotFoundException( "this vehicle is not found at vehicles table"));

            if(!Objects.equals(choosenVehicle.getUser().getId() , userDetails.getUser().getId() )){
                logger.error(String.format("AccessDeniedException : the user with id : %d , was trying to access vehicle with plate number %s" ,
                userDetails.getId() , choosenVehicle.getPlateNumber()));
                throw new AccessDeniedException("You do not have permission to use this vehicle.");          
            }

            logger.info(choosenVehicle.getUser().getId() + "\n" + userDetails.getUser().getId());

        
            slotService.addVehicleToAnEmptySlot(ReservationUserRequest.slotId(), ReservationUserRequest.vehicleId());

            Reservation newReservation = ReservationMapper.toEntity(ReservationUserRequest);
            requiredSlot.setVehicle(choosenVehicle);
            newReservation.setSlot(requiredSlot);
            newReservation.setUser(userDetails.getUser());
            newReservation.setGarage(requiredSlot.getGarage());
            Reservation savedReservation = reservationRepository.save(newReservation);

            double parkingFee = calculateFees(savedReservation);

           
            Map<String, Object> reservationBill = new HashMap<>();
            reservationBill.put("reservation_details", ReservationMapper.toResponseDto(savedReservation));
            reservationBill.put("parking_fee", parkingFee);

            
            return reservationBill ;
      
    }

    public Map<String , Object> confirmReservation(byte[] imageBytes){
        String text = qrCodeService.readQRCode(imageBytes);
        // format ex : G1_S2
        String[] parts = text.split("_");

        String garageIdStr = parts[0].substring(1); // skip 'G' for garage
        int garageId = Integer.parseInt(garageIdStr);

        String slotNumberStr = parts[1].substring(1); // skip 'S' for slot
        int slotNumber = Integer.parseInt(slotNumberStr);

        Slot slot = slotRepository.findBySlotNumber(slotNumber)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Slot not found: " + slotNumber));

        Reservation pendingReservation = reservationRepository
            .findActiveReservation(garageId, slot.getId(), Reservation.Status.PENDING)
            .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No active reservation for slot %d in garage %d",
                            slotNumber, garageId)));

         Map<String , Object> confirmationInformation = new HashMap<>();   
         confirmationInformation.put("Reservation", pendingReservation);
         confirmationInformation.put("Garage Id", garageId);
         confirmationInformation.put("Slot number", slotNumber);

         return confirmationInformation;
        
    }

    public Page<ReservationResponse> getReservations(
            Integer userId,
            Integer slotId,
            Integer garageId,
            Reservation.Status status,
            LocalDateTime start,
            LocalDateTime end,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<Reservation> spec = Specification.where(ReservationSpecs.hasUser(userId))
                .and(ReservationSpecs.hasSlotId(slotId))
                .and(ReservationSpecs.hasGarageId(garageId))
                .and(ReservationSpecs.hasStatus(status))
                .and(ReservationSpecs.betweenTime(start, end));

        return reservationRepository.findAll(spec, pageable).map(ReservationMapper::toResponseDto);
    }

    public void deleteReservation(int id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id " + id);
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    public ReservationResponse updateReservation(int id, ReservationUserRequest dto) {

        Reservation reservation =
                reservationRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (dto.slotId() != null) {
            Slot slot =
                    slotRepository
                            .findById(dto.slotId())
                            .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
            reservation.setSlot(slot);
        }

        if (dto.garageId() != null) {
            Garage garage =
                    garageRepository
                            .findById(dto.garageId())
                            .orElseThrow(() -> new ResourceNotFoundException("Garage not found"));

            reservation.setGarage(garage);
        }

        if (dto.startingTime() != null) {
            reservation.setStartingTime(dto.startingTime());
        }

        if (dto.endingTime() != null) {
            reservation.setEndingTime(dto.endingTime());
        }

        Reservation updatedReservation = reservationRepository.save(reservation);
        return ReservationMapper.toResponseDto(updatedReservation);
    }
}

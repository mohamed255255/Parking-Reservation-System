package com.parking_reservation_system.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.google.zxing.qrcode.encoder.QRCode;
import com.parking_reservation_system.dto.request.ReservationDto;
import com.parking_reservation_system.dto.request.VehicleDto;
import com.parking_reservation_system.dto.response.ReservationResponseDto;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.ReservationMapper;
import com.parking_reservation_system.mapper.VehicleMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.*;
import com.parking_reservation_system.service.payment.PaymentService;
import com.parking_reservation_system.specification.ReservationSpecs;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReservationService {

    private final GarageRepository garageRepository;
    // since it is mvp project i put hourlyPricec in env file
    @Value("${parking.price.hourly}")
    private int hourlyPrice;
  
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final QRCodeService qrCodeService ;
    private final VehicleRepository vehicleRepository ;


    ReservationService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }


    public double calculateFees(ReservationResponseDto newReservation) {
        LocalTime start = newReservation.startingTime();
        LocalTime end   = newReservation.endingTime();
        long minutes = Duration.between(start, end).toMinutes();
        double hours = Math.ceil((minutes / 60.0) * 100) / 100;
        return hourlyPrice * hours;
    }
    
    public ReservationResponseDto createReservation(ReservationDto reservationDto , int vehicleId) {
       /// can i block two concurrent reservations 

            User currentUser  =  userRepository.findById(reservationDto.user_id()).orElseThrow(() -> new ResourceNotFoundException("user id is not found in the DB"));
            Slot requiredSlot =  slotRepository.findById(reservationDto.slot_id()).orElseThrow(() -> new ResourceNotFoundException("slot id is not found in the DB"));
            Vehicle choosenVehicle =  vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("the vehicle is not found")) ;

            if(requiredSlot.getVehicle() != null){
                    throw new RuntimeException("this slot is already occupied") ;
            }

            Reservation newReservation = ReservationMapper.toEntity(reservationDto);
            // place the vehicle on the slot
            requiredSlot.setVehicle(choosenVehicle);
            // link the data 
            newReservation.setSlot(requiredSlot);
            newReservation.setUser(currentUser);
            newReservation.setGarage(requiredSlot.getGarage());

            var reservationEntity = reservationRepository.save(newReservation);
           
            return ReservationMapper.toResponseDto(reservationEntity) ;
    }

    // this API should trigger the payment without it it is expired
    public void confirmReservation(byte[] imageBytes) throws IOException{
        // scan QR code
        String text = qrCodeService.readQRCode(imageBytes);
        // format ex : G1_S2
        String[] parts = text.split("_");
      
        String garageIdStr = parts[0].substring(1); // skip 'G'
        int garageId = Integer.parseInt(garageIdStr);
            
        String slotNumberStr = parts[1].substring(1); // skip 'S'
        int slotNumber = Integer.parseInt(slotNumberStr);
        
        Slot slot = slotRepository.findBySlotNumber(slotNumber).get();
        
        // get the tied reservation and check if it is active
        reservationRepository.findActiveReservation(garageId , slot.getId(), Reservation.Status.PENDING)
        .orElseThrow(
            () -> new ResourceNotFoundException("there is no reservation active for slot number : " + slotNumber + " garage id : " + garageId)
        );
    
        // Redirect to payment page that has 
        // pay button which will transfer the user to the gateway IFrame
    }



    public Page<ReservationResponseDto> getUserReservations(
            Integer userId,
            Integer slotId,
            Integer garageId,
            Reservation.Status status,
            LocalDateTime start,
            LocalDateTime end,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<Reservation> spec = Specification
                .where(ReservationSpecs.hasUser(userId))
                .and(ReservationSpecs.hasSlotId(slotId))
                .and(ReservationSpecs.hasGarageId(garageId))
                .and(ReservationSpecs.hasStatus(status))
                .and(ReservationSpecs.betweenTime(start, end));

        return reservationRepository.findAll(spec, pageable)
                .map(ReservationMapper::toResponseDto);
    }



    public Page<ReservationResponseDto> getAllReservations(
        Integer slotId,
        Integer garageId,
        Reservation.Status status,
        LocalDateTime start,
        LocalDateTime end,
        int page,
        int size
) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

    Specification<Reservation> spec = Specification
            .where(ReservationSpecs.hasSlotId(slotId))
            .and(ReservationSpecs.hasGarageId(garageId))
            .and(ReservationSpecs.hasStatus(status))
            .and(ReservationSpecs.betweenTime(start, end));

    return reservationRepository.findAll(spec, pageable)
            .map(ReservationMapper::toResponseDto);
}


public void deleteReservation(Integer id) {
    if (!reservationRepository.existsById(id)) {
        throw new ResourceNotFoundException("Reservation not found with id " + id);
    }
    reservationRepository.deleteById(id);
}



public ReservationResponseDto updateReservation(Integer id, ReservationDto dto) {
    Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

    if (dto.slot_id() != null) {
        reservation.setSlot(slotRepository.findById(dto.slot_id())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found")));
    }

    if (dto.garage_id() != null) {
        reservation.setGarage(garageRepository.findById(dto.garage_id())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found")));
    }

    if (dto.getStatus() != null) {
        reservation.setStatus(dto.());
    }

    if (dto.getStartingTime() != null) {
        reservation.setStartingTime(dto.getStartingTime());
    }

    if (dto.getEndingTime() != null) {
        reservation.setEndingTime(dto.getEndingTime());
    }

    Reservation updated = reservationRepository.save(reservation);
    return ReservationMapper.toResponseDto(updated);
}








}

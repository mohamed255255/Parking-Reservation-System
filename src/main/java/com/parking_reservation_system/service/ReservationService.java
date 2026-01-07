package com.parking_reservation_system.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.google.zxing.qrcode.encoder.QRCode;
import com.parking_reservation_system.dto.request.ReservationDto;
import com.parking_reservation_system.dto.request.VehicleDto;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.mapper.ReservationMapper;
import com.parking_reservation_system.mapper.VehicleMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.ReservationRepository;
import com.parking_reservation_system.repository.SlotRepository;
import com.parking_reservation_system.repository.UserRepository;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.security.CustomUserDetails;
import com.parking_reservation_system.service.payment.PaymentService;

import ch.qos.logback.classic.LoggerContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReservationService {
    // since it is mvp project i put hourlyPricec in env file
    @Value("${parking.price.hourly}")
    private int hourlyPrice;
  
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final QRCodeService qrCodeService ;
    private final VehicleRepository vehicleRepository ;
    private final SlotService slotService ;


    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    public double calculateFees(Reservation newReservation) {
        LocalTime start = newReservation.getStartingTime();
        LocalTime end   = newReservation.getEndingTime();
        long minutes = Duration.between(start, end).toMinutes();
        double hours = Math.ceil((minutes / 60.0) * 100) / 100;
        return hourlyPrice * hours;
    }
    
    @Transactional
    public Optional<Reservation> createRequest(
        CustomUserDetails userDetails ,
        ReservationDto reservationDto ,
        int vehicleId) {   

        try {
    
            Slot requiredSlot      =  slotRepository.findByIdWithALock(reservationDto.slot_id())
            .orElseThrow(() -> new ResourceNotFoundException("this slot is not found in the slots table"));
           
            Vehicle choosenVehicle =  vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException("this vehicle is not found at the vechicles table")) ;

            if(requiredSlot.getVehicle() != null){
                    throw new RuntimeException("this slot is already occupied") ;
            }
            /// if there is a problem here the whole reservation service will rollback
            slotService.addVehicleToAnEmptySlot( reservationDto.slot_id() , vehicleId);

            Reservation newReservation = ReservationMapper.toEntity(reservationDto);
          
            requiredSlot.setVehicle(choosenVehicle);

            newReservation.setSlot(requiredSlot);
            newReservation.setUser(userDetails.getUser());
            newReservation.setGarage(requiredSlot.getGarage());
    
            Reservation savedReservation = reservationRepository.save(newReservation);
           
            return Optional.of(savedReservation);
        }catch (Exception ex) {
             throw new RuntimeException(ex.getMessage());
            }

    }

    
    public String confirmReservation(byte[] imageBytes) throws IOException{
        /// scan QR code
        String text = qrCodeService.readQRCode(imageBytes);
        // format ex : G1_S2
        String[] parts = text.split("_");
      
        String garageIdStr = parts[0].substring(1); // skip 'G'
        int garageId = Integer.parseInt(garageIdStr);
            
        String slotNumberStr = parts[1].substring(1); // skip 'S'
        int slotNumber = Integer.parseInt(slotNumberStr);
        
        Slot slot = slotRepository.findBySlotNumber(slotNumber).get();
        // get the tied reservation and check if it is active
        var reservation = reservationRepository.findActiveReservation(garageId , slot.getId(), Reservation.Status.PENDING).orElseThrow(
            () -> new ResourceNotFoundException("there is no reservation active for slot number : " + slotNumber + " garage id : " + garageId)
        );
    
        // Redirect to payment page
        return "reservation has been created" ;

    }












}

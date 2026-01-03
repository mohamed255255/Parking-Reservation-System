package com.parking_reservation_system.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
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
import com.parking_reservation_system.service.payment.PaymentService;

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
    private final UserRepository userRepository;
    private final QRCodeService qrCodeService ;
    private final PaymentService paymentService ;
    private final VehicleRepository vehicleRepository ;


    public double calculateFees(Reservation newReservation) {
        LocalTime start = newReservation.getStartingTime();
        LocalTime end   = newReservation.getEndingTime();
        long minutes = Duration.between(start, end).toMinutes();
        double hours = Math.ceil((minutes / 60.0) * 100) / 100;
        return hourlyPrice * hours;
    }
    
    public Optional<Reservation> createRequest(ReservationDto reservationDto , int vehicleId) {
       /// can i block two concurrent reservations 
       
        try {
        
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

            Reservation savedReservation = reservationRepository.save(newReservation);
           
            return Optional.of(savedReservation);
        }catch (DataIntegrityViolationException ex) {
            return (Optional.empty());
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

package com.garage_system.Service;

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

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.DTO.request.VehicleDto;
import com.garage_system.Model.Reservation;
import com.garage_system.Model.Slot;
import com.garage_system.Model.User;
import com.garage_system.Model.Vehicle;
import com.garage_system.Repository.ReservationRepository;
import com.garage_system.Repository.SlotRepository;
import com.garage_system.Repository.UserRepository;
import com.garage_system.Repository.VehicleRepository;
import com.garage_system.exception.ResourceNotFoundException;
import com.garage_system.mapper.ReservationMapper;
import com.garage_system.mapper.VehicleMapper;
import com.google.zxing.qrcode.encoder.QRCode;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class ReservationService {
    // since it is mvp project i put hourlyPricec in env file
    @Value("${parking.price.hourly}")
    private int hourlyPrice;
  
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final QRCodeService qrCodeService ;

    public double calculateFees(Reservation newReservation) {
        LocalTime start = newReservation.getStartingTime();
        LocalTime end   = newReservation.getEndingTime();
        long minutes = Duration.between(start, end).toMinutes();
        double hours = Math.ceil((minutes / 60.0) * 100) / 100;
        return hourlyPrice * hours;
    }
    
    public Optional<Reservation> createRequest(ReservationDto reservationDto , VehicleDto vehicleDto) {
        try {
        
            User currentUser  =  userRepository.findById(reservationDto.getUser_id()).orElseThrow(() -> new ResourceNotFoundException("user id is not found in the DB"));
            Slot requiredSlot =  slotRepository.findById(reservationDto.getSlot_id()).orElseThrow(() -> new ResourceNotFoundException("slot id is not found in the DB"));
            Vehicle choosenVehicle = VehicleMapper.toEntity(vehicleDto);

            if(requiredSlot.getVehicle() != null){
                    throw new RuntimeException("this slot is already occupied") ;
            }

            Reservation newReservation = ReservationMapper.toEntity(reservationDto);
            requiredSlot.setVehicle(choosenVehicle);
           
            newReservation.setSlot(requiredSlot);
            newReservation.setUser(currentUser);
           
            Reservation savedReservation = reservationRepository.save(newReservation);
           
            return Optional.of(savedReservation);
        }catch (DataIntegrityViolationException ex) {
            return (Optional.empty());
            }
    }

    
    public void confirmReservation(){
       
    }












}

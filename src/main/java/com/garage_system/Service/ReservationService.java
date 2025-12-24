package com.garage_system.Service;

import java.math.BigDecimal;
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


@Service
public class ReservationService {
   
    @Value("${parking.price.hourly}")
    private BigDecimal hourlyPrice;
  
    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              SlotRepository slotRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
    }

    
    public ResponseEntity<?> createRequest(ReservationDto reservationDto , VehicleDto vehicleDto) {
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
            // return response of the reservation again which will act as recipt 
            // and add the price to id  price * hours
           
            return ResponseEntity.ok(savedReservation);
        }catch (DataIntegrityViolationException ex) {
            return ResponseEntity.ok((Optional.empty()));
            }
    }


}

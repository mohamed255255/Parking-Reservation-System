package com.parking_reservation_system.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.parking_reservation_system.dto.request.ReservationDto;
import com.parking_reservation_system.dto.response.ReservationResponseDto;
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

import jakarta.transaction.Transactional;
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
    private final QRCodeService qrCodeService ;
    private final VehicleRepository vehicleRepository ;
    private final SlotService slotService ;


    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);



    public double calculateFees(ReservationResponseDto reservation) {
        LocalDateTime start = reservation.startingTime();
        LocalDateTime end   = reservation.endingTime();
        long minutes = Duration.between(start, end).toMinutes();
        double hours = Math.ceil((minutes / 60.0) * 100) / 100;
        return hourlyPrice * hours;
    }
    
    @Transactional
    public ReservationResponseDto createReservation(
        CustomUserDetails userDetails ,
        ReservationDto reservationDto ,
        int vehicleId) {   

        try {
    
            Slot requiredSlot      =  slotRepository.findByIdWithALock(reservationDto.slot_id())
            .orElseThrow(() -> new ResourceNotFoundException("this slot is not found in the slots table"));
           
            Vehicle choosenVehicle =  vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new ResourceNotFoundException("this vehicle is not found at the vechicles table")) ;

            /// check vehicle ownership
            if(choosenVehicle.getUser() != userDetails.getUser())
                 throw new ResourceNotFoundException("the user does not possess this vehicle") ;
          
            /// if there is a problem here the whole reservation service will rollback
            slotService.addVehicleToAnEmptySlot( reservationDto.slot_id() , vehicleId);

            Reservation newReservation = ReservationMapper.toEntity(reservationDto);
          
            requiredSlot.setVehicle(choosenVehicle);

            newReservation.setSlot(requiredSlot);
            newReservation.setUser(userDetails.getUser());
            newReservation.setGarage(requiredSlot.getGarage());
            Reservation savedReservation = reservationRepository.save(newReservation);
            return ReservationMapper.toResponseDto(savedReservation);

        }catch (Exception ex) {
             throw new RuntimeException(ex.getMessage());
        }

    }

    
    public void confirmReservation(byte[] imageBytes) throws IOException{
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
    ){
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

   
    @Transactional
    public ReservationResponseDto patchReservation(Integer id, ReservationDto dto) {

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (dto.slot_id() != null) {
            Slot slot = slotRepository.findById(dto.slot_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
            reservation.setSlot(slot);
        }

        if (dto.garage_id() != null) {
            Garage garage = garageRepository.findById(dto.garage_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Garage not found"));
            
            reservation.setGarage(garage);
        }

        if (dto.startingTime() != null) {
            reservation.setStartingTime(dto.startingTime());
        }

        if (dto.endingTime() != null) {
            reservation.setEndingTime(dto.endingTime());
        }

        Reservation updated = reservationRepository.save(reservation);
        return ReservationMapper.toResponseDto(updated);
    }

}











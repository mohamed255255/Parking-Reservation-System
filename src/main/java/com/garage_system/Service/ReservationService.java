package com.garage_system.Service;

import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.Model.Reservation;
import com.garage_system.Model.Slot;
import com.garage_system.Model.User;
import com.garage_system.Repository.ReservationRepository;
import com.garage_system.Repository.SlotRepository;
import com.garage_system.Repository.UserRepository;
import com.garage_system.exception.ResourceNotFoundException;
import com.garage_system.mapper.ReservationMapper;


@Service
public class ReservationService {

  
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

    public Optional<Reservation> createRequest(ReservationDto reservationDto) {
        try {
        
            User relatedUser =  userRepository.findById(reservationDto.getUser_id()).orElseThrow(() -> new ResourceNotFoundException("no user id is sent with this reservation"));
            Slot relatedSlot =  slotRepository.findById(reservationDto.getSlot_id()).orElseThrow(() -> new ResourceNotFoundException("no slot id is sent with this reservation"));
           
            Reservation newReservation = ReservationMapper.toEntity(reservationDto);
           
            newReservation.setSlot(relatedSlot);
            newReservation.setUser(relatedUser);
           
            Reservation savedReservation = reservationRepository.save(newReservation);
        
            return Optional.of(savedReservation);
        } catch (DataIntegrityViolationException ex) {
            return Optional.empty();
            }
    }


}

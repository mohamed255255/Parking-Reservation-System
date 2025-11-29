package com.garage_system.Service;

import java.lang.foreign.Linker.Option;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.Model.Reservation;
import com.garage_system.Repository.ReservationRepository;
import com.garage_system.mapper.ReservationMapper;

public class ReservationService {

    private final ReservationRepository reservationRepository ;
    
    public ReservationService( ReservationRepository reservationRepository){
        this.reservationRepository =  reservationRepository ;
    }

    public Optional<Reservation> createRequest(ReservationDto reservationDto) {
        try {
            Reservation savedReservation = reservationRepository.save(ReservationMapper.toEntity(reservationDto));
            return Optional.of(savedReservation);
        } catch (DataIntegrityViolationException ex) {
            return Optional.empty();
        }
    }


}

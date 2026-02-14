package com.parking_reservation_system.scheduler;

import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.parking_reservation_system.repository.ReservationRepository;
import com.parking_reservation_system.repository.SlotRepository;

import org.springframework.transaction.annotation.Transactional;

@Component
class ReleaseSlotJob {

     ReservationRepository reservationRepository ;
     SlotRepository slotRepository;

    public ReleaseSlotJob(ReservationRepository reservationRepository , SlotRepository slotRepository ){
        this.slotRepository        = slotRepository ;
        this.reservationRepository = reservationRepository ;
    }

    // every 3 min i will check the end time for every car 
    @Transactional
    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
    public void CheckEndTimeAndReleaseSlot(){
        /// check all reservation table where now() > end time 
       var reservations = reservationRepository.findEndedReservationsToday();
         for(var r : reservations){
            var slot = r.getSlot() ;
            slot.setVehicle(null);   /// release the slot 
        }
    }
}
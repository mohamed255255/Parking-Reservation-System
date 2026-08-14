package com.parking_reservation_system.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.zxing.WriterException;
import com.parking_reservation_system.dto.request.SlotDto;
import com.parking_reservation_system.dto.response.SlotResponseDto;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.repository.GarageRepository;
import com.parking_reservation_system.repository.SlotRepository;
import com.parking_reservation_system.repository.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class SlotServiceTest {
    @Mock
    private  SlotRepository slotRepository;
    @Mock
    private  GarageRepository garageRepository ;
    @Mock
    private  VehicleRepository vehicleRepository ;
    @Mock
    private  QRCodeService qrCodeService ;

    @InjectMocks
    SlotService slotService ;

    @Test //// TODO: After refactor remove this since we catch inside the methods 
    public void should_create_slot_successfully() throws IOException, WriterException {
       
        final int SLOT_NUMBER = 2;
        final int SLOT_WIDTH  = 5;
        final int SLOT_DEPTH  = 10;
        final int GARAGE_ID   = 1;
        final int VEHICLE_ID  = 123;

        SlotDto dummySlotDto = new SlotDto(SLOT_NUMBER, SLOT_WIDTH, SLOT_DEPTH, GARAGE_ID, VEHICLE_ID);
        Garage dummyGarage = new Garage();
        
       
        Slot savedSlot = new Slot();
        savedSlot.setGarage(dummyGarage);
        savedSlot.setQrCodeImagePath("/images/qr_123.png");

        // Mock 1: Garage lookup
        when(garageRepository.findById(dummySlotDto.garage_id()))
                .thenReturn(Optional.of(dummyGarage));

        // Mock 2: QR Code generation
        when(qrCodeService.saveQRCodeImage(dummySlotDto))
                .thenReturn("/images/qr_123.png");

        // Mock 3: Saving to DB
        when(slotRepository.save(any(Slot.class)))
                .thenReturn(savedSlot);

        SlotResponseDto response = slotService.createSlot(dummySlotDto);
        assertNotNull(response);
    }

    @Test
    public void should_get_all_user_slots_successfully(){
        User dummyUser = new User(Integer id, String name, String email, String password , String phone);
        dummyUser.setVehicles(List<Vehicle>.of(""))
        Slot s = new Slot() ;
        s.setVehicle();
        
        when(slotRepository.getUserSlotsAndVehicles(dummyUser.getId())).then(new List<>)
    }

}
package com.parking_reservation_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.parking_reservation_system.exception.QRCodeGenerationException;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.GarageRepository;
import com.parking_reservation_system.repository.SlotRepository;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.util.SecurityTestUtils;
import com.parking_reservation_system.util.SlotTestFactory;
import com.parking_reservation_system.util.UserTestFactory;
import com.parking_reservation_system.util.VehicleTestFactory;

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

    @Test
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

        when(garageRepository.findById(dummySlotDto.garage_id()))
                .thenReturn(Optional.of(dummyGarage));

        when(qrCodeService.saveQRCodeImage(dummySlotDto))
                .thenReturn("/images/qr_123.png");

        when(slotRepository.save(any(Slot.class)))
                .thenReturn(savedSlot);

        SlotResponseDto response = slotService.createSlot(dummySlotDto);
        assertNotNull(response);
    }

    @Test 
    public void should_throw_IOexception_on_create_slot(){
        Garage dummyGarage = new Garage() ;
        SlotDto slotDto = SlotTestFactory.createSlotDto(dummyGarage);

        when(garageRepository.findById(slotDto.garage_id()))
                    .thenReturn(Optional.of(dummyGarage));
             
        when(qrCodeService.saveQRCodeImage(slotDto)).thenThrow(new IOException("Disk I/O failure"));
     
        QRCodeGenerationException exception = assertThrows(
            QRCodeGenerationException.class,
            () -> slotService.createSlot(slotDto)
    );

    assertEquals("failed to create QR code for the slot ", exception.getMessage());
    assertInstanceOf(IOException.class, exception.getCause());
    verify(slotRepository, never()).save(any(Slot.class));
    }

    @Test
    public void should_get_all_user_slots_successfully() {
        User dummyUser = UserTestFactory.createRandomUser();
        Garage dummyGarage = new Garage() ;
    
        Vehicle dummyVehicle =  VehicleTestFactory.createTestVehicleForUser(dummyUser);
        Slot slotOne = SlotTestFactory.createOccupiedSlot(dummyGarage , dummyVehicle);
    
        SecurityTestUtils.mockSecurityContext(dummyUser);
        when(slotRepository.getUserSlotsAndVehicles(dummyUser.getId()))
                .thenReturn(List.of(slotOne));

        List<SlotResponseDto> response = slotService.getUserSlots();

        assertNotNull(response);
        assertEquals(1, response.size());
    }

}
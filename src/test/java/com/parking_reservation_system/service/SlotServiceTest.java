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

import com.google.zxing.WriterException;
import com.parking_reservation_system.dto.request.SlotRequest;
import com.parking_reservation_system.dto.response.SlotResponse;
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
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SlotServiceTest {
    @Mock private SlotRepository slotRepository;
    @Mock private GarageRepository garageRepository;
    @Mock private VehicleRepository vehicleRepository;
    @Mock private QRCodeService qrCodeService;

    @InjectMocks SlotService slotService;

    @Test
    public void should_create_slot_successfully() throws IOException, WriterException {

        final int SLOT_NUMBER = 2;
        final int SLOT_WIDTH = 5;
        final int SLOT_DEPTH = 10;
        final int GARAGE_ID = 1;
        final int VEHICLE_ID = 123;

        SlotRequest dummySlotRequest =
                new SlotRequest(SLOT_NUMBER, SLOT_WIDTH, SLOT_DEPTH, GARAGE_ID, VEHICLE_ID);
        Garage dummyGarage = new Garage();

        Slot savedSlot = new Slot();
        savedSlot.setGarage(dummyGarage);
        savedSlot.setQrCodeImagePath("/images/qr_123.png");

        when(garageRepository.findById(dummySlotRequest.garageId()))
                .thenReturn(Optional.of(dummyGarage));

        when(qrCodeService.saveQRCodeImage(dummySlotRequest)).thenReturn("/images/qr_123.png");

        when(slotRepository.save(any(Slot.class))).thenReturn(savedSlot);

        SlotResponse response = slotService.createSlot(dummySlotRequest);
        assertNotNull(response);
    }

    @Test
    public void should_throw_IOexception_on_create_slot() {
        Garage dummyGarage = new Garage();
        SlotRequest SlotRequest = SlotTestFactory.createSlotRequest(dummyGarage);

        when(garageRepository.findById(SlotRequest.garageId()))
                .thenReturn(Optional.of(dummyGarage));

        when(qrCodeService.saveQRCodeImage(SlotRequest))
                .thenThrow(new IOException("Disk I/O failure"));

        QRCodeGenerationException exception =
                assertThrows(
                        QRCodeGenerationException.class, () -> slotService.createSlot(SlotRequest));

        assertEquals("failed to create QR code for the slot ", exception.getMessage());
        assertInstanceOf(IOException.class, exception.getCause());
        verify(slotRepository, never()).save(any(Slot.class));
    }

    @Test
    public void should_get_all_user_slots_successfully() {
        User dummyUser = UserTestFactory.createRandomUser();
        Garage dummyGarage = new Garage();

        Vehicle dummyVehicle = VehicleTestFactory.createTestVehicleForUser(dummyUser);
        Slot slotOne = SlotTestFactory.createOccupiedSlot(dummyGarage, dummyVehicle);

        SecurityTestUtils.mockSecurityContext(dummyUser);
        when(slotRepository.getUserSlotsAndVehicles(dummyUser.getId()))
                .thenReturn(List.of(slotOne));

        List<SlotResponse> response = slotService.getUserSlots();

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void should_add_vehicle_to_empty_slot() {
        // Given
        Garage garage = new Garage();
        garage.setId(1);

        Vehicle dummyVehicle = VehicleTestFactory.createTestVehicleForUser(new User());

        Slot dummySlot = SlotTestFactory.createEmptyLargeSlot(garage);

        // When
        when(slotRepository.findById(dummySlot.getId())).thenReturn(Optional.of(dummySlot));
        when(vehicleRepository.findById(dummyVehicle.getId()))
                .thenReturn(Optional.of(dummyVehicle));

        slotService.addVehicleToAnEmptySlot(dummySlot.getId(), dummyVehicle.getId());
        // Then
        assertEquals(dummyVehicle, dummySlot.getVehicle());
        verify(slotRepository, times(1)).save(dummySlot);
    }

    @Test
    void should_fail_when_vehicle_exceeds_slot_dimensions() {
        Slot smallSlot = SlotTestFactory.createEmptySmallSlot(new Garage());
        Vehicle vehicle = VehicleTestFactory.createTestVehicleForUser(new User());

        when(slotRepository.findById(smallSlot.getId())).thenReturn(Optional.of(smallSlot));
        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

        assertThrows(
                RuntimeException.class,
                () -> slotService.addVehicleToAnEmptySlot(smallSlot.getId(), vehicle.getId()));

        // VERIFY SIDE EFFECT (Database shouldn't persist invalid state)
        verify(slotRepository, never()).save(any());
    }

    @Test
    void should_fail_adding_vehicle_to_busy_slot() {
        Slot dummySlot = SlotTestFactory.createEmptySmallSlot(new Garage());
        Vehicle existedVehicle = VehicleTestFactory.createTestVehicleForUser(new User());
        Vehicle newVehicle = VehicleTestFactory.createTestVehicleForUser(new User());
        dummySlot.setVehicle(existedVehicle);

        when(slotRepository.findById(dummySlot.getId())).thenReturn(Optional.of(dummySlot));
        when(vehicleRepository.findById(newVehicle.getId())).thenReturn(Optional.of(newVehicle));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                slotService.addVehicleToAnEmptySlot(
                                        dummySlot.getId(), newVehicle.getId()));

        assertEquals(
                "the slot number " + dummySlot.getId() + " is already busy",
                exception.getMessage());
        verify(slotRepository, never()).save(any());
    }
}

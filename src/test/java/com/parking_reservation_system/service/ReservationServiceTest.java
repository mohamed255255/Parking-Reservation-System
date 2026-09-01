package com.parking_reservation_system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.parking_reservation_system.dto.request.ReservationUserRequest;
import com.parking_reservation_system.dto.response.ReservationResponse;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.GarageRepository;
import com.parking_reservation_system.repository.ReservationRepository;
import com.parking_reservation_system.repository.SlotRepository;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.security.CustomUserDetails;
import com.parking_reservation_system.util.SlotTestFactory;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    
    @Mock
    private  GarageRepository garageRepository;
    @Mock
    private  ReservationRepository reservationRepository;
    @Mock
    private  SlotRepository slotRepository;
    @Mock
    private  QRCodeService qrCodeService;
    @Mock
    private  VehicleRepository vehicleRepository;
    @Mock
    private  SlotService slotService;
    
    @InjectMocks    
    private  ReservationService reservationService ;
 
 
@Test
public void reservation_should_be_created_successfully() {
    User user = new User();
    user.setId(100);

    CustomUserDetails userDetails = mock(CustomUserDetails.class);
    when(userDetails.getUser()).thenReturn(user);

    Vehicle vehicle = new Vehicle();
    vehicle.setId(1);
    vehicle.setUser(user);

    Garage garage = new Garage();
    garage.setId(10);

    Slot slot = SlotTestFactory.createEmptyLargeSlot(garage);
    slot.setId(1);

    ReservationUserRequest request = 
            new ReservationUserRequest(LocalDateTime.now(), LocalDateTime.now().plusHours(2), slot.getId(), garage.getId() , vehicle.getId());

    when(slotRepository.findByIdWithALock(request.slotId())).thenReturn(Optional.of(slot));
    when(vehicleRepository.findById(request.vehicleId())).thenReturn(Optional.of(vehicle));
    when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
   
    Map<String, Object> reservationBill = reservationService.createReservation(userDetails, request);

    ReservationResponse response = (ReservationResponse) reservationBill.get("reservation_details");
    assertThat(response.slotId()).isEqualTo(slot.getId());
    assertThat(response.garageId()).isEqualTo(garage.getId());
    assertThat(response.userId()).isEqualTo(user.getId());

    verify(slotService).addVehicleToAnEmptySlot(request.slotId(), vehicle.getId());
    verify(reservationRepository).save(any(Reservation.class));
}
  
@Test
public void should_throw_ResourceNotFoundException_if_vehicle_not_found() {
    final int NON_EXISTENT_VEHICLE_ID = 99;
    Garage dummyGarage = new Garage();
    Slot dummySlot = SlotTestFactory.createEmptyLargeSlot(dummyGarage);
    dummySlot.setId(1);

    ReservationUserRequest request = 
            new ReservationUserRequest(LocalDateTime.now(), LocalDateTime.now().plusHours(2), dummySlot.getId(), dummyGarage.getId() , NON_EXISTENT_VEHICLE_ID);

    CustomUserDetails userDetails = mock(CustomUserDetails.class);

    when(slotRepository.findByIdWithALock(request.slotId()))
            .thenReturn(Optional.of(dummySlot));
    when(vehicleRepository.findById(NON_EXISTENT_VEHICLE_ID))
            .thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class, 
            () -> reservationService.createReservation(userDetails, request)
    );

    assertThat(exception.getMessage()).isEqualTo("this vehicle is not found at vehicles table");

    verify(slotService, never()).addVehicleToAnEmptySlot(anyInt(), anyInt());
    verify(reservationRepository, never()).save(any(Reservation.class));
}

@Test
public void should_prevent_duplicate_reservation_for_same_slot() {
    User user = new User();
    user.setId(100);

    CustomUserDetails userDetails = mock(CustomUserDetails.class);
    when(userDetails.getUser()).thenReturn(user);

    Vehicle vehicle = new Vehicle();
    vehicle.setId(1);
    vehicle.setUser(user);

    Garage garage = new Garage();
    garage.setId(10);

    Slot slot = SlotTestFactory.createEmptyLargeSlot(garage);
    slot.setId(5);

    ReservationUserRequest request = 
            new ReservationUserRequest(LocalDateTime.now(), LocalDateTime.now().plusHours(2), slot.getId(), garage.getId() , vehicle.getId());

    when(slotRepository.findByIdWithALock(request.slotId())).thenReturn(Optional.of(slot));
    when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));

    doThrow(new IllegalStateException("Slot is already occupied"))
            .when(slotService).addVehicleToAnEmptySlot(slot.getId(), vehicle.getId());

    IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> reservationService.createReservation(userDetails, request)
    );

    assertThat(exception.getMessage()).isEqualTo("Slot is already occupied");

    // Verify reservation was never saved due to duplicate attempt
    verify(reservationRepository, never()).save(any(Reservation.class));
}
    
@Test
void should_confirm_reservation_successfully() {

    byte[] sampleBytes = "fake-qr-bytes".getBytes(StandardCharsets.UTF_8);
    String qrText = "G1_S2"; 

    Slot dummySlot = new Slot();
    dummySlot.setId(10);
    dummySlot.setSlotNumber(2);

    Reservation dummyReservation = new Reservation();
    dummyReservation.setId(100);
    dummyReservation.setStatus(Reservation.Status.PENDING);

    when(qrCodeService.readQRCode(sampleBytes)).thenReturn(qrText);
    when(slotRepository.findBySlotNumber(2)).thenReturn(Optional.of(dummySlot));
    when(reservationRepository.findActiveReservation(1, 10, Reservation.Status.PENDING))
            .thenReturn(Optional.of(dummyReservation));

    Map<String, Object> result = reservationService.confirmReservation(sampleBytes);

    assertThat(result).isNotNull();
    assertThat(result).containsEntry("Garage Id", 1);
    assertThat(result).containsEntry("Slot number", 2);
    assertThat(result).containsEntry("Reservation", dummyReservation);
}

@Test
public void should_confirm_reservation_throw_ResourceNotFoundException_for_no_pending_reservation() {
    byte[] sampleBytes = "fake-qr-bytes".getBytes(StandardCharsets.UTF_8);
    String qrText = "G1_S2"; 
    final int GARAGE_ID = 1;
    final int SLOT_NUMBER = 2;

    Slot dummySlot = new Slot();
    dummySlot.setId(10);
    dummySlot.setSlotNumber(SLOT_NUMBER);

    when(qrCodeService.readQRCode(sampleBytes)).thenReturn(qrText);
    when(slotRepository.findBySlotNumber(SLOT_NUMBER)).thenReturn(Optional.of(dummySlot));
    when(reservationRepository.findActiveReservation(GARAGE_ID, dummySlot.getId(), Reservation.Status.PENDING))
            .thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> reservationService.confirmReservation(sampleBytes)
    );

    assertThat(exception.getMessage()).isEqualTo(String.format("No active reservation for slot %d in garage %d",
    SLOT_NUMBER ,GARAGE_ID));
}

@Test
public void should_get_all_reservations_when_not_providing_page_and_size_and_user() {
    final int defaultPage = 0;
    final int defaultSize = 10;
   
    User dummyUser = new User() ;
    dummyUser.setId(1);

    Reservation reservation1 = new Reservation();
    reservation1.setId(1);
    reservation1.setSlot(new Slot());
    reservation1.setGarage(new Garage());
    reservation1.setUser(dummyUser);

    Reservation reservation2 = new Reservation();
    reservation2.setId(2);
    reservation2.setSlot(new Slot());
    reservation2.setGarage(new Garage());
    reservation2.setUser(dummyUser);

    Page<Reservation> reservationPage = new PageImpl<>(List.of(reservation1, reservation2));

    when(reservationRepository.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(reservationPage);

            User user =  new User() ;
            user.setId(1);
    Page<ReservationResponse> result = reservationService.getReservations(
          user.getId() ,        // userId
            null,        // slotId
            null,        // garageId
            null,        // status
            null,        // start
            null,        // end
            defaultPage, // page
            defaultSize  // size
    );

    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(2);

    verify(reservationRepository).findAll(any(Specification.class), any(Pageable.class));
}

@Test
public void should_get_paginated_reservations_using_filter() {
    final int PAGE_NUMBER = 1;
    final int PAGE_SIZE = 5;
    final Integer USER_ID = 100;
    final Integer FIRST_SLOT_ID = 5;
    final Integer SECOND_SLOT_ID = 6;
    final Integer GARAGE_ID = 10;
    final Reservation.Status STATUS = Reservation.Status.PENDING;
    final LocalDateTime START_TIME = LocalDateTime.now();
    final LocalDateTime END_TIME = START_TIME.plusHours(5);

    User dummyUser = new User();
    dummyUser.setId(USER_ID);

    Garage dummyGarage = new Garage();
    dummyGarage.setId(GARAGE_ID);

    Slot firstDummySlot = new Slot();
    firstDummySlot.setId(FIRST_SLOT_ID);

    Slot secondDummySlot = new Slot();
    secondDummySlot.setId(SECOND_SLOT_ID);

    
    Reservation reservationOne = new Reservation();
    reservationOne.setId(1);
    reservationOne.setStatus(STATUS);
    reservationOne.setSlot(firstDummySlot);
    reservationOne.setGarage(dummyGarage);
    reservationOne.setUser(dummyUser);

    
    Reservation reservationTwo = new Reservation();
    reservationTwo.setId(2);
    reservationTwo.setStatus(STATUS);
    reservationTwo.setSlot(secondDummySlot);
    reservationTwo.setGarage(dummyGarage);
    reservationTwo.setUser(dummyUser);

    Pageable expectedPageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, Sort.by("createdAt").descending());
   
    Page<Reservation> reservationPage = new PageImpl<>(List.of(reservationOne), expectedPageable, 1);

    when(reservationRepository.findAll(any(Specification.class), eq(expectedPageable)))
            .thenReturn(reservationPage);

    Page<ReservationResponse> result = reservationService.getReservations(
            USER_ID,
            FIRST_SLOT_ID,
            GARAGE_ID,
            STATUS,
            START_TIME,
            END_TIME,
            PAGE_NUMBER,
            PAGE_SIZE
    );

    assertThat(result).isNotNull();
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).slotId()).isEqualTo(FIRST_SLOT_ID);
    assertThat(result.getNumber()).isEqualTo(PAGE_NUMBER);
    assertThat(result.getSize()).isEqualTo(PAGE_SIZE);

    verify(reservationRepository).findAll(any(Specification.class), eq(expectedPageable));
}
    
@Test
void should_update_only_slot_and_leave_other_fields_untouched() {
    final int OLD_GARAGE_ID = 200;
    final int OLD_SLOT_ID = 100;
    final int NEW_SLOT_ID = 555;

    Garage garage = new Garage();
    garage.setId(OLD_GARAGE_ID);

    Slot oldSlot = SlotTestFactory.createEmptySmallSlot(garage);
    oldSlot.setId(OLD_SLOT_ID);

    Slot newSlot = SlotTestFactory.createEmptySmallSlot(garage);
    newSlot.setId(NEW_SLOT_ID);

    User reservationOwner = new User() ;
    reservationOwner.setId(1);
    
    Reservation reservation = new Reservation();
    reservation.setUser(reservationOwner);
    reservation.setId(3);
    reservation.setSlot(oldSlot);
    reservation.setGarage(garage);

    when(reservationRepository.findById(reservation.getId()))
            .thenReturn(Optional.of(reservation));
    when(slotRepository.findById(NEW_SLOT_ID)).thenReturn(Optional.of(newSlot));
    when(reservationRepository.save(any(Reservation.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    ReservationUserRequest dto = new ReservationUserRequest(null, null, NEW_SLOT_ID, null , null );

    reservationService.updateReservation(reservation.getId(), dto);

    ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
    verify(reservationRepository).save(captor.capture());
    Reservation saved = captor.getValue();

    assertThat(saved.getSlot().getId()).isEqualTo(NEW_SLOT_ID); // changed
    assertThat(saved.getGarage().getId()).isEqualTo(OLD_GARAGE_ID); // untouched

}
  

}

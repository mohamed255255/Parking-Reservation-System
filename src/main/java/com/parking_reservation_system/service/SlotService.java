package com.parking_reservation_system.service;

import com.parking_reservation_system.dto.request.SlotRequest;
import com.parking_reservation_system.dto.response.SlotResponse;
import com.parking_reservation_system.exception.InvalidDimensionsException;
import com.parking_reservation_system.exception.ResourceNotFoundException;
import com.parking_reservation_system.exception.SlotBusyException;
import com.parking_reservation_system.mapper.SlotMapper;
import com.parking_reservation_system.model.Garage;
import com.parking_reservation_system.model.Slot;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.model.Vehicle;
import com.parking_reservation_system.repository.GarageRepository;
import com.parking_reservation_system.repository.SlotRepository;
import com.parking_reservation_system.repository.VehicleRepository;
import com.parking_reservation_system.security.CustomUserDetails;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SlotService {

    private final SlotRepository slotRepository;
    private final GarageRepository garageRepository;
    private final VehicleRepository vehicleRepository;
    private final QRCodeService qrCodeService;

    public SlotResponse createSlot(SlotRequest SlotRequest) {

        Garage existedGarage =
                garageRepository
                        .findById(SlotRequest.garageId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        String.format("Garage not found with id: %d", SlotRequest.garageId())));

        Slot newSlot = SlotMapper.toEntity(SlotRequest);
        newSlot.setGarage(existedGarage);

        String qrCodePath = qrCodeService.saveQRCodeImage(SlotRequest);
        newSlot.setQrCodeImagePath(qrCodePath);

        return SlotMapper.toResponseDto(slotRepository.save(newSlot));
    }

    public List<SlotResponse> getUserSlots() {

        User currentAuthUser =
                ((CustomUserDetails)
                                SecurityContextHolder.getContext()
                                        .getAuthentication()
                                        .getPrincipal())
                        .getUser();

        if (currentAuthUser == null) throw new ResourceNotFoundException("User is not found");

        return slotRepository.getUserSlotsAndVehicles(currentAuthUser.getId()).stream()
                .map(slot -> SlotMapper.toResponseDto(slot))
                .toList();
    }

    public SlotResponse getSlotById(int id) {
        return slotRepository
                .findById(id)
                .map(slot -> SlotMapper.toResponseDto(slot))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Slot not found with id: %d", id)));
    }

    public void addVehicleToAnEmptySlot(int slotId, int vehicleId) {
        Slot slot = slotRepository.findById(slotId).get();
        Vehicle vehicle = vehicleRepository.findById(vehicleId).get();

        boolean isEmpty = slot.getVehicle() == null;
        if (isEmpty) {
            if (vehicle.getVehicleDepth() <= slot.getSlotDepth()
                    && vehicle.getVehicleWidth() <= slot.getSlotWidth()) {

                Vehicle myVehicle =
                        vehicleRepository
                                .findById(vehicle.getId())
                                .orElseThrow(
                                        () ->
                                                new ResourceNotFoundException(
                                                        "Vehicle is not found"));
                slot.setVehicle(myVehicle);
                slotRepository.save(slot);
                return;
            } else {
                throw new InvalidDimensionsException(String.format("Expected fitting vehicle inside the slot of area (%d * %d) (Width * depth), but got vehicle of width : %d and depth %d "
                , slot.getSlotWidth() , slot.getSlotDepth() ,vehicle.getVehicleWidth()  , vehicle.getVehicleDepth()));
            }
        }
        throw new SlotBusyException(String.format("the slot number %d is already busy", slotId));
    }
}

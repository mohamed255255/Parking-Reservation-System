package com.parking_reservation_system.repository;

import com.parking_reservation_system.model.Vehicle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            """
        UPDATE Vehicle v
        SET v.plateNumber = :plateNumber,
            v.modelYear = :modelYear,
            v.modelName = :modelName,
            v.vehicleWidth = :vehicleWidth,
            v.vehicleDepth = :vehicleDepth,
            v.type = :vehicleType
        WHERE v.id = :id
    """)
    void updateVehicle(
            @Param("plateNumber") String plateNumber,
            @Param("modelYear") int modelYear,
            @Param("modelName") String modelName,
            @Param("vehicleWidth") double vehicleWidth,
            @Param("vehicleDepth") double vehicleDepth,
            @Param("vehicleType") String vehicleType,
            @Param("id") int id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Vehicle v WHERE v.id = :id")
    void deleteVehicle(@Param("id") int id);

}

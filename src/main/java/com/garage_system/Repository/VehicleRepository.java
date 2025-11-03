package com.garage_system.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.garage_system.Model.Vehicle;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
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
        @Param("id") int id
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Vehicle v WHERE v.id = :id")
    void deleteVehicle(@Param("id") int id);
 
}

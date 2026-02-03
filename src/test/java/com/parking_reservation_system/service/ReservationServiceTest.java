package com.parking_reservation_system.service;
public class ReservationServiceTest {
    /// happy path tests :
    
    /// test create reservation for user
    /// required slot is valid (empty and not tied with a vehicle and its dimensions are fitting)
    /// confirm Reservation is created successfully
    /// getUserReservations using dynamic paginated filtered query
    

    /// edge cases tests:
    
    /// required slot is not valid (not empty or tied with a vehicle or its dimensions are not fitting)
    /// vehicle is not found
    /// user is not found
    /// user does not possess the vehicle
    /// slot is not found
    /// double Reservation for the same slot from two different users
    /// double Reservation for the same slot from the same user
    /// double Reservation for the same slot from the same user but with different vehicle
    
}

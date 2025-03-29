package com.example.flightsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface FlightRepository extends JpaRepository<FlightEntity, Long> {

    Optional<FlightEntity> findByUniqueKey(String uniqueKey);

    @Query("SELECT DISTINCT f FROM FlightEntity AS f WHERE f.departureAirport = :departureAirport AND f.departureTime = :departureTime AND f.airline = :airline AND f.arrivalTime = :arrivalTime AND f.arrivalAirport = :arrivalAirport")
    Optional<FlightEntity> findUniqueFlights(
            @Param("departureAirport") String departureAirport,
            @Param("departureTime") String departureTime,
            @Param("airline") String airline,
            @Param("arrivalTime") String arrivalTime,
            @Param("arrivalAirport") String arrivalAirport
    );
}

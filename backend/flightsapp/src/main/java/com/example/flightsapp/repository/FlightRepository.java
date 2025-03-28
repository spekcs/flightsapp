package com.example.flightsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
    Optional<FlightEntity> findByDepartureAirportAndDepartureTimeAndDate(String departureAirport, String departureTime, String date);
}

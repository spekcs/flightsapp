package com.example.flightsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
}

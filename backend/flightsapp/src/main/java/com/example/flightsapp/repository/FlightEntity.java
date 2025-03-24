package com.example.flightsapp.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FlightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer flightId;
    private String departureAirport;
    private String arrivalAirport;
    private String date;
    private String departureTime;
    private String arrivalTime;
    private Integer flightTimeMinutes;
    private String airline;
}

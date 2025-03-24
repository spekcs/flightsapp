package com.example.flightsapp.repository;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FlightEntity {
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime dateTime;
    private Integer flightTimeMinutes;
    private String airline;
    private Double price;
}

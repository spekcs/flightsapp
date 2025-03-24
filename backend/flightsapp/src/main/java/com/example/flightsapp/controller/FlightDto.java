package com.example.flightsapp.controller;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDto {
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime dateTime;
    private Integer flightTimeMinutes;
    private String airline;
    private Double price;
}

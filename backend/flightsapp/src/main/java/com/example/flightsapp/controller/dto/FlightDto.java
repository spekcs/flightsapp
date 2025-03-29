package com.example.flightsapp.controller.dto;


import java.util.UUID;

public record FlightDto (
        Long flightId,
    String departureAirport,
    String arrivalAirport,
    String date,
    String departureTime,
    String arrivalTime,
    Integer flightTimeMinutes,
    String airline){
}

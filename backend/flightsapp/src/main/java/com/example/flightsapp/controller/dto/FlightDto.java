package com.example.flightsapp.controller.dto;


public record FlightDto (
    String departureAirport,
    String arrivalAirport,
    String date,
    String departureTime,
    String arrivalTime,
    Integer flightTimeMinutes,
    String airline){
}

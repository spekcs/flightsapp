package com.example.flightsapp.controller.dto;

public record FlightsAPIFlight (
    String airline,
    String arrival_airport,
    String arrival_time,
    String departure_airport,
    String departure_time,
    String flight_date) {
}

package com.example.flightsapp.controller;

import lombok.Data;


@Data
public class FlightDto {
    private String departureAirport;
    private String arrivalAirport;
    private String date;
    private String departureTime;
    private String arrivalTime;
    private Integer flightTimeMinutes;
    private String airline;
}

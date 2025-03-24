package com.example.flightsapp.controller.dto;

import lombok.Data;

@Data
public class FlightsAPIFlight {
    String airline;
    String arrival_airport;
    String arrival_time;
    String departure_airport;
    String departure_time;
    String flight_data;
}

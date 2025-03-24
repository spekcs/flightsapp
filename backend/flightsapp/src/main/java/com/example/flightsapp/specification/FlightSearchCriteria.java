package com.example.flightsapp.specification;

import java.time.LocalDateTime;

public record FlightSearchCriteria (
        String departureAirport,
        String arrivalAirport,
        LocalDateTime dateTime,
        Integer flightTimeMinutes,
        String airline,
        Double price
){
}

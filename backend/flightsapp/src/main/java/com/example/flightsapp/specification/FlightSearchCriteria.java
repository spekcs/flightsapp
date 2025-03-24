package com.example.flightsapp.specification;


import java.time.LocalDate;

public record FlightSearchCriteria (
        Integer pageOffset,
        Integer limit,
        String date,
        String timeStart,
        String timeEnd,
        String departureAirport,
        String arrivalAirport,
        String airline,
        String sortBy,
        String direction
){
}

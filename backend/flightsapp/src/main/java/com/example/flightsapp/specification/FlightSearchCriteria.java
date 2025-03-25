package com.example.flightsapp.specification;


public record FlightSearchCriteria (
        Integer pageOffset,
        Integer limit,
        String date,
        String timeStart,
        String timeEnd,
        String departureAirport,
        String arrivalAirport,
        String airline
){
}


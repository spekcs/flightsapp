package com.example.flightsapp.controller.dto;

import java.util.List;


public record FlightsAPIResponseObject (
    List<FlightsAPIFlight> data,
    int error,
    FlightsAPIPagination pagination) {
}

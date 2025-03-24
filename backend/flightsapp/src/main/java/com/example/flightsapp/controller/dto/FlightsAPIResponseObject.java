package com.example.flightsapp.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


public record FlightsAPIResponseObject (
    List<FlightsAPIFlight> data,
    int error,
    FlightsAPIPagination pagination) {
}

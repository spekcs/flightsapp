package com.example.flightsapp.controller.dto;

public record FlightsAPIPagination (
    int count,
    int limit,
    int offset,
    int total) {
}

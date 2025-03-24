package com.example.flightsapp.controller.dto;

import lombok.Data;

public record FlightsAPIPagination (
    int count,
    int limit,
    int offset,
    int total) {
}

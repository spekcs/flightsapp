package com.example.flightsapp.controller.dto;

import com.example.flightsapp.controller.FlightDto;

import java.util.List;

public record PageResponse<T> (
        List<T> content,
        Integer pageOffset,
        Integer count,
        Integer total
) {
}

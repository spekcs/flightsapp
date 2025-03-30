package com.example.flightsapp.controller.dto;

import java.util.List;

public record BookingDto(
        List<String> seatCodes
) {
}

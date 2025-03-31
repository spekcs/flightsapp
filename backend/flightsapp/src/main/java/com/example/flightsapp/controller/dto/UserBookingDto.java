package com.example.flightsapp.controller.dto;

import java.util.List;

public record UserBookingDto(
        FlightDto flight,
        List<SeatDto> seats
) {
}

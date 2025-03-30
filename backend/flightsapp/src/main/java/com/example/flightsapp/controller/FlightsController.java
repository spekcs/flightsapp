package com.example.flightsapp.controller;

import com.example.flightsapp.controller.dto.BookingDto;
import com.example.flightsapp.controller.dto.FlightDto;
import com.example.flightsapp.controller.dto.PageResponse;
import com.example.flightsapp.service.FlightsService;
import com.example.flightsapp.specification.FlightSearchCriteria;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightsController {
    private final FlightsService flightsService;

    @GetMapping()
    public PageResponse<FlightDto> getFlights(@ModelAttribute FlightSearchCriteria criteria) {
        return flightsService.getFlights(criteria);
    }

    @PostMapping("/book/{id}")
    public ResponseEntity<?> bookFlight(@PathVariable("id") Long flightId, @RequestBody BookingDto bookingDto, HttpServletRequest request) {
        return flightsService.bookFlight(flightId, bookingDto, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getFlight(@PathVariable("id") Long flightId) {
        return flightsService.getFlight(flightId);
    }

}

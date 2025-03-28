package com.example.flightsapp.controller;

import com.example.flightsapp.controller.dto.FlightDto;
import com.example.flightsapp.controller.dto.PageResponse;
import com.example.flightsapp.service.FlightsService;
import com.example.flightsapp.specification.FlightSearchCriteria;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> test(@PathVariable Long id) {
        return flightsService.getFlight(id);
    }
}

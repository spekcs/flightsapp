package com.example.flightsapp.controller;

import com.example.flightsapp.service.FlightsService;
import com.example.flightsapp.specification.FlightSearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightsController {
    private final FlightsService flightsService;

    @GetMapping()
    public ResponseEntity<List<FlightDto>> getFlights(@ModelAttribute FlightSearchCriteria criteria) {
        return flightsService.getFlights(criteria);
    }
}

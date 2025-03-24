package com.example.flightsapp.controller;

import com.example.flightsapp.controller.dto.FlightDto;
import com.example.flightsapp.controller.dto.FlightsAPIFlight;
import com.example.flightsapp.controller.dto.FlightsAPIResponseObject;
import com.example.flightsapp.controller.dto.PageResponse;
import com.example.flightsapp.service.FlightsService;
import com.example.flightsapp.specification.FlightSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightsController {
    private final FlightsService flightsService;
    @Autowired
    private final RestTemplate restTemplate;

    @GetMapping()
    public PageResponse<FlightDto> getFlights(@ModelAttribute FlightSearchCriteria criteria) {
        return flightsService.getFlights(criteria);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(restTemplate.getForObject("http://localhost:3000/", FlightsAPIResponseObject.class));
    }
}

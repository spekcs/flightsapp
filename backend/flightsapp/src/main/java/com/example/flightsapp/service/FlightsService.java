package com.example.flightsapp.service;

import com.example.flightsapp.controller.FlightDto;
import com.example.flightsapp.controller.dto.PageResponse;
import com.example.flightsapp.specification.FlightSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightsService {
    public PageResponse<FlightDto> getFlights(FlightSearchCriteria criteria) {
        return null;
    }
}

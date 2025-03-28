package com.example.flightsapp.controller;

import com.example.flightsapp.controller.dto.SeatDto;
import com.example.flightsapp.service.SeatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class SeatsController {
    private final SeatsService seatsService;
    @GetMapping("/{flightId}")
    public ResponseEntity<List<SeatDto>> getSeats(@PathVariable Long flightId) {
        return seatsService.getSeats(flightId);
    }
}

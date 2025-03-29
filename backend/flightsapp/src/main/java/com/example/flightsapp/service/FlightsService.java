package com.example.flightsapp.service;

import com.example.flightsapp.controller.dto.FlightDto;
import com.example.flightsapp.controller.dto.FlightsAPIFlight;
import com.example.flightsapp.controller.dto.FlightsAPIResponseObject;
import com.example.flightsapp.controller.dto.PageResponse;
import com.example.flightsapp.exception.ExternalAPIException;
import com.example.flightsapp.exception.NotFoundException;
import com.example.flightsapp.mapping.FlightsMapper;
import com.example.flightsapp.repository.FlightEntity;
import com.example.flightsapp.repository.FlightRepository;
import com.example.flightsapp.specification.FlightSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;


@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
public class FlightsService {
    private final RestTemplate restTemplate;
    private final FlightsMapper flightsMapper;
    private final FlightRepository flightRepository;
    private final Logger logger = LoggerFactory.getLogger(FlightsService.class);

    public PageResponse<FlightDto> getFlights(FlightSearchCriteria criteria) {
        String uri = "http://localhost:3000";
        UriComponents builder = UriComponentsBuilder.fromUri(URI.create(uri))
                .queryParamIfPresent("page_offset", Optional.ofNullable(criteria.pageOffset()))
                .queryParamIfPresent("limit", Optional.ofNullable(criteria.limit()))
                .queryParamIfPresent("date", Optional.ofNullable(criteria.date()))
                .queryParamIfPresent("time_start", Optional.ofNullable(criteria.timeStart()))
                .queryParamIfPresent("time_end", Optional.ofNullable(criteria.timeEnd()))
                .queryParamIfPresent("dept_airport", Optional.ofNullable(criteria.departureAirport()))
                .queryParamIfPresent("arr_airport", Optional.ofNullable(criteria.arrivalAirport()))
                .queryParamIfPresent("airline", Optional.ofNullable(criteria.airline()))
                .build();

        FlightsAPIResponseObject response = restTemplate.getForObject(builder.toUriString(), FlightsAPIResponseObject.class);
        if (response == null) {
            throw new ExternalAPIException("Flights API didn't return data.");
        }
        if (response.error() != 200) {
            throw new ExternalAPIException(String.format("External API error, code %s", response.error()));
        }

        List<FlightEntity> existingFlights = new ArrayList<>();
        List<FlightEntity> newFlights = new ArrayList<>();
        for (FlightsAPIFlight flight : response.data()) {
            String flightKey = String.format(
                    "%s|%s|%s|%s|%s",
                    flight.departure_airport().trim().toLowerCase(),
                    flight.departure_time(),
                    flight.airline().trim().toLowerCase(),
                    flight.arrival_time(),
                    flight.arrival_airport().trim().toLowerCase()
            );

            Optional<FlightEntity> optionalFlightEntity = flightRepository.findByUniqueKey(flightKey);

            if (optionalFlightEntity.isPresent()) {
                existingFlights.add(optionalFlightEntity.get());
            } else {
                FlightEntity flightEntity = flightsMapper.toEntity(flight);
                flightEntity.setUniqueKey(flightKey);
                newFlights.add(flightEntity);
            }
        }
        flightRepository.saveAllAndFlush(newFlights);
        existingFlights.addAll(newFlights);

        return new PageResponse<>(flightsMapper.toDtoList(existingFlights), response.pagination().offset(), response.pagination().limit(), response.pagination().count(), response.pagination().total());
    }

    public ResponseEntity<FlightDto> getFlight(Long id) {
        Optional<FlightEntity> flightEntity = flightRepository.findById(id);
        if (flightEntity.isEmpty()) {
            throw new NotFoundException("Flight not found.");
        }
        return ResponseEntity.ok(flightsMapper.toDto(flightEntity.get()));
    }
}

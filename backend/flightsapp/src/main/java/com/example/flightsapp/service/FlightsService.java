package com.example.flightsapp.service;

import com.example.flightsapp.controller.dto.*;
import com.example.flightsapp.exception.ApplicationException;
import com.example.flightsapp.exception.ExternalAPIException;
import com.example.flightsapp.exception.NotFoundException;
import com.example.flightsapp.mapping.FlightsMapper;
import com.example.flightsapp.repository.*;
import com.example.flightsapp.specification.FlightSearchCriteria;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class FlightsService {
    private final RestTemplate restTemplate;
    private final FlightsMapper flightsMapper;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final UserIdService userIdService;
    private final BookedSeatRepository bookedSeatRepository;

    @Value("${external.api.url}")
    private String uri;

    public PageResponse<FlightDto> getFlights(FlightSearchCriteria criteria) {
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
        FlightsAPIResponseObject response;
        try {
            response = restTemplate.getForObject(builder.toUriString(), FlightsAPIResponseObject.class);
        } catch (Exception e) {
            throw new ExternalAPIException(e.getMessage());
        }
        if (response == null) {
            throw new ExternalAPIException("Flights API didn't return data.");
        }
        if (response.error() != 200) {
            throw new ExternalAPIException(String.format("External API error, code %s", response.error()));
        }

        List<FlightEntity> flightsList = new ArrayList<>();
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
                flightsList.add(optionalFlightEntity.get());
            } else {
                FlightEntity flightEntity = flightsMapper.toEntity(flight);
                flightEntity.setUniqueKey(flightKey);
                flightRepository.saveAndFlush(flightEntity);
            }
        }
        List<FlightDto> flightDtoList = flightsMapper.toDtoList(flightsList);
        if (criteria.orderBy() != null) {
            if (criteria.orderBy().equalsIgnoreCase("departureTime")) {
                flightDtoList.sort(Comparator.comparing(FlightDto::departureTime));
            } else if (criteria.orderBy().equalsIgnoreCase("flightTime")) {
                flightDtoList.sort(Comparator.comparing(FlightDto::flightTimeMinutes));
            }
        }


        return new PageResponse<>(flightDtoList, response.pagination().offset(), response.pagination().limit(), response.pagination().count(), response.pagination().total());
    }

    public ResponseEntity<FlightDto> getFlight(Long id) {
        Optional<FlightEntity> flightEntity = flightRepository.findById(id);
        if (flightEntity.isEmpty()) {
            throw new NotFoundException("Flight not found.");
        }
        return ResponseEntity.ok(flightsMapper.toDto(flightEntity.get()));
    }

    public ResponseEntity<FlightDto> bookFlight(Long id, BookingDto bookingDto, HttpServletRequest request) {
        Optional<FlightEntity> flightEntity = flightRepository.findById(id);
        if (flightEntity.isEmpty()) {
            throw new NotFoundException("Flight not found.");
        }
        Long userId = userIdService.extractUserId(request);
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found.");
        }

        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setFlight(flightEntity.get());
        bookingEntity.setUser(user.get());

        if (bookingDto.seatCodes().isEmpty()) {
            throw new ApplicationException("Booking must include at least one seat.");
        }

        for (String seatCode : bookingDto.seatCodes()) {
            BookedSeatEntity bookedSeatEntity = new BookedSeatEntity();
            bookedSeatEntity.setSeatCode(seatCode);
            bookedSeatEntity.setBooking(bookingEntity);
            bookedSeatEntity.setFlight(flightEntity.get());
            bookedSeatRepository.save(bookedSeatEntity);
        }

        bookingRepository.save(bookingEntity);
        return ResponseEntity.accepted().build();
    }
}

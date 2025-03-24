package com.example.flightsapp.service;

import com.example.flightsapp.controller.dto.FlightDto;
import com.example.flightsapp.controller.dto.FlightsAPIFlight;
import com.example.flightsapp.controller.dto.FlightsAPIResponseObject;
import com.example.flightsapp.controller.dto.PageResponse;
import com.example.flightsapp.exception.ExternalAPIException;
import com.example.flightsapp.mapping.FlightsMapper;
import com.example.flightsapp.specification.FlightSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FlightsService {
    @Autowired
    private final RestTemplate restTemplate;
    private final FlightsMapper flightsMapper;

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

        return new PageResponse<>(flightsMapper.toDtoList(response.data()), response.pagination().offset(), response.pagination().limit(), response.pagination().count(), response.pagination().total());
    }
}

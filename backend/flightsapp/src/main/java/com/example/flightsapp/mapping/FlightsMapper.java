package com.example.flightsapp.mapping;

import com.example.flightsapp.controller.dto.FlightDto;
import com.example.flightsapp.controller.dto.FlightsAPIFlight;
import com.example.flightsapp.repository.FlightEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlightsMapper {
    FlightDto toDto(FlightEntity flightEntity);
    FlightEntity toEntity(FlightDto flightDto);
    List<FlightDto> toDtoList(List<FlightsAPIFlight> flightEntities);

    default FlightDto toDto(FlightsAPIFlight flightsAPIFlight) {
        int departureTimeMinutes = 60 * Integer.parseInt(flightsAPIFlight.departure_time().substring(0,2)) + Integer.parseInt(flightsAPIFlight.departure_time().substring(3));
        int arrivalTimeMinutes = 60 * Integer.parseInt(flightsAPIFlight.arrival_time().substring(0,2)) + Integer.parseInt(flightsAPIFlight.arrival_time().substring(3));
        int flightTimeMinutes = arrivalTimeMinutes - departureTimeMinutes;
        return new FlightDto(
                flightsAPIFlight.departure_airport(),
                flightsAPIFlight.arrival_airport(),
                flightsAPIFlight.flight_date(),
                flightsAPIFlight.departure_time(),
                flightsAPIFlight.arrival_time(),
                flightTimeMinutes,
                flightsAPIFlight.airline()
        );
    }

}

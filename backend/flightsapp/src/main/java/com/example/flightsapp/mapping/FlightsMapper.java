package com.example.flightsapp.mapping;

import com.example.flightsapp.controller.dto.FlightDto;
import com.example.flightsapp.controller.dto.FlightsAPIFlight;
import com.example.flightsapp.repository.FlightEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlightsMapper {
    default FlightEntity toEntity(FlightsAPIFlight flightsAPIFlight) {
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setDepartureAirport(flightsAPIFlight.departure_airport());
        flightEntity.setDepartureTime(flightsAPIFlight.departure_time());
        flightEntity.setAirline(flightsAPIFlight.airline());
        flightEntity.setArrivalAirport(flightsAPIFlight.arrival_airport());
        flightEntity.setArrivalTime(flightsAPIFlight.arrival_time());
        flightEntity.setDate(flightsAPIFlight.flight_date());
        return flightEntity;
    }
    List<FlightDto> toDtoList(List<FlightEntity> flightEntities);

    default FlightDto toDto(FlightEntity flightEntity) {
        int departureTimeMinutes = 60 * Integer.parseInt(flightEntity.getDepartureTime().substring(11,13)) + Integer.parseInt(flightEntity.getDepartureTime().substring(14,16));
        int arrivalTimeMinutes = 60 * Integer.parseInt(flightEntity.getArrivalTime().substring(11,13)) + Integer.parseInt(flightEntity.getArrivalTime().substring(14,16));
        int flightTimeMinutes = arrivalTimeMinutes - departureTimeMinutes;
        return new FlightDto(
                flightEntity.getId(),
                flightEntity.getDepartureAirport(),
                flightEntity.getArrivalAirport(),
                flightEntity.getDate(),
                flightEntity.getDepartureTime(),
                flightEntity.getArrivalTime(),
                flightTimeMinutes,
                flightEntity.getAirline()
        );
    }

}

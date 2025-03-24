package com.example.flightsapp.mapping;

import com.example.flightsapp.controller.FlightDto;
import com.example.flightsapp.repository.FlightEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlightsMapper {
    FlightDto toDto(FlightEntity flightEntity);
    FlightEntity toEntity(FlightDto flightDto);
    List<FlightDto> toDtoList(List<FlightEntity> flightEntities);

}

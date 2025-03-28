package com.example.flightsapp.mapping;

import com.example.flightsapp.controller.dto.SeatDto;
import com.example.flightsapp.repository.BookedSeatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeatsMapper {
    List<SeatDto> toDtoList(List<BookedSeatEntity> entityList);

    default SeatDto toDto(BookedSeatEntity entity) {
        return new SeatDto(entity.getBookedSeatId(), entity.getSeatCode(), SeatDto.Type.getType(entity.getSeatCode()));
    }
}

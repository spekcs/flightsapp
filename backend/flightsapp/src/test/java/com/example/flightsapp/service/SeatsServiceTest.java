package com.example.flightsapp.service;

import com.example.flightsapp.controller.dto.SeatDto;
import com.example.flightsapp.controller.dto.SeatRecommendationDto;
import com.example.flightsapp.exception.ApplicationException;
import com.example.flightsapp.mapping.SeatsMapper;
import com.example.flightsapp.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatsServiceTest {
    @Mock
    private BookedSeatRepository bookedSeatRepository;

    @InjectMocks
    private SeatsService seatsService;

    @Test
    void testGetRecommendation_SuccessTOGETHER() {
        SeatRecommendationDto seatRecommendationDto = new SeatRecommendationDto(3L, SeatRecommendationDto.Type.TOGETHER);

        when(bookedSeatRepository.findByFlightId(1L)).thenReturn(
            getBookedSeats(List.of("1A", "2B", "2A", "2C", "7A"))
        );

        ResponseEntity<List<SeatDto>> response = seatsService.getRecommendation(1L, seatRecommendationDto);
        assertEquals(response, ResponseEntity.ok(List.of(new SeatDto("2A"), new SeatDto("2B"), new SeatDto("2C"))));
    }

    @Test
    void testGetRecommendation_NotEnoughSeats() {
        SeatRecommendationDto seatRecommendationDto = new SeatRecommendationDto(3L, SeatRecommendationDto.Type.TOGETHER);

        when(bookedSeatRepository.findByFlightId(1L)).thenReturn(
                getBookedSeats(List.of())
        );

        assertThrows(ApplicationException.class, () -> seatsService.getRecommendation(1L, seatRecommendationDto));
    }

    @Test
    void testGetRecommendation_SuccessWINDOW() {
        SeatRecommendationDto seatRecommendationDto = new SeatRecommendationDto(2L, SeatRecommendationDto.Type.WINDOW);

        when(bookedSeatRepository.findByFlightId(1L)).thenReturn(
                getBookedSeats(List.of("1A", "1B", "1C", "1D"))
        );

        ResponseEntity<List<SeatDto>> response = seatsService.getRecommendation(1L, seatRecommendationDto);
        assertEquals(response, ResponseEntity.ok(List.of(new SeatDto("1A"), new SeatDto("1D"))));
    }

    private List<BookedSeatEntity> getBookedSeats(List<String> availableSeats) {
        List<BookedSeatEntity> seatEntities = new ArrayList<>();
        FlightEntity flightEntity = new FlightEntity();
        flightEntity.setId(1L);
        for (Integer row : IntStream.range(1, 34).boxed().toList()) {
            for (String seat : List.of("A", "B", "C", "D")) {
                String seatCode = String.format("%d%s", row, seat);
                if (!availableSeats.contains(seatCode)) {
                    BookedSeatEntity bookedSeatEntity = new BookedSeatEntity();
                    bookedSeatEntity.setSeatCode(seatCode);
                    bookedSeatEntity.setBooking(new BookingEntity());
                    bookedSeatEntity.setFlight(flightEntity);
                    seatEntities.add(bookedSeatEntity);
                }
            }
        }
        return seatEntities;
    }
}

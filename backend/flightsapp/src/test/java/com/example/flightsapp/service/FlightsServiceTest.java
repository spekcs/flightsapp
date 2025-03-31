package com.example.flightsapp.service;

import com.example.flightsapp.controller.dto.*;
import com.example.flightsapp.exception.ApplicationException;
import com.example.flightsapp.exception.NotFoundException;
import com.example.flightsapp.mapping.FlightsMapper;
import com.example.flightsapp.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightsServiceTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private FlightsMapper flightsMapper;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserIdService userIdService;
    @Mock
    private BookedSeatRepository bookedSeatRepository;

    @InjectMocks
    private FlightsService flightsService;


    @Test
    void testGetFlight_Success() {
        Optional<FlightEntity> flight = Optional.of(new FlightEntity());
        FlightDto dto = new FlightDto(1L, "","","","","",0,"");

        when(flightRepository.findById(1L)).thenReturn(flight);
        when(flightsMapper.toDto(flight.get())).thenReturn(dto);

        ResponseEntity<FlightDto> response = flightsService.getFlight(1L);

        then(flightsMapper).should().toDto(flight.get());
        then(flightRepository).should().findById(1L);
        assertEquals(response, new ResponseEntity<>(dto, HttpStatus.OK));
    }

    @Test
    void testGetFlight_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> flightsService.getFlight(1L));
    }

    @Test
    void testBookFlight_Success() {
        HttpServletRequest request = new MockHttpServletRequest();
        Optional<FlightEntity> flight = Optional.of(new FlightEntity());
        UserEntity user = new UserEntity();
        BookingDto bookingDto = new BookingDto(List.of("1A"));

        when(flightRepository.findById(1L)).thenReturn(flight);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userIdService.extractUserId(request)).thenReturn(1L);

        ResponseEntity<?> response = flightsService.bookFlight(1L, bookingDto, request);

        then(bookedSeatRepository).should().save(any());
        then(bookingRepository).should().save(any());

        assertEquals(response, ResponseEntity.accepted().build());
    }

    @Test
    void testBookFlight_EmptyBookingThrows() {
        HttpServletRequest request = new MockHttpServletRequest();
        Optional<FlightEntity> flight = Optional.of(new FlightEntity());
        UserEntity user = new UserEntity();
        BookingDto bookingDto = new BookingDto(List.of());

        when(flightRepository.findById(1L)).thenReturn(flight);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userIdService.extractUserId(request)).thenReturn(1L);

        assertThrows(ApplicationException.class, () -> flightsService.bookFlight(1L, bookingDto, request));
    }
}

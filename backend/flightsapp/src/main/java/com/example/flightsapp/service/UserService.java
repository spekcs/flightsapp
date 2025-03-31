package com.example.flightsapp.service;

import com.example.flightsapp.controller.dto.*;
import com.example.flightsapp.exception.ApplicationException;
import com.example.flightsapp.exception.NotFoundException;
import com.example.flightsapp.mapping.FlightsMapper;
import com.example.flightsapp.mapping.SeatsMapper;
import com.example.flightsapp.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserIdService userIdService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final FlightsMapper flightsMapper;
    private final BookedSeatRepository bookedSeatRepository;
    private final SeatsMapper seatsMapper;

    public ResponseEntity<LoginResponseDto> register(UserAuthDto userAuthDto) {
        if (userAuthDto.username() == null || userAuthDto.password() == null) {
            throw new ApplicationException("Username and password are required");
        }

        if (userRepository.existsByUsername(userAuthDto.username())) {
            throw new ApplicationException("Username is already taken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(userAuthDto.username());
        user.setPassword(passwordEncoder.encode(userAuthDto.password()));
        userRepository.save(user);
        return ResponseEntity.ok(new LoginResponseDto(userIdService.generateToken(user)));
    }

    public ResponseEntity<LoginResponseDto> login(UserAuthDto userAuthDto) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(userAuthDto.username());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User with this username and password combination not found");
        }
        UserEntity user = optionalUser.get();
        if (!passwordEncoder.matches(userAuthDto.password(), user.getPassword())) {
            throw new NotFoundException("User with this username and password combination not found");
        }
        String token = userIdService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    public ResponseEntity<List<UserBookingDto>> getBookings(HttpServletRequest request) {
        Optional<UserEntity> optionalUser = userRepository.findById(userIdService.extractUserId(request));
        if (optionalUser.isEmpty()) {
            throw new ApplicationException("Invalid token.");
        }
        List<UserBookingDto> bookings = new ArrayList<>();
        List<BookingEntity> bookingEntities = bookingRepository.findAllByUser(optionalUser.get());
        for (BookingEntity bookingEntity : bookingEntities) {
            FlightEntity flight = bookingEntity.getFlight();
            List<BookedSeatEntity> seats = bookedSeatRepository.findAllByBooking(bookingEntity);
            UserBookingDto bookingDto = new UserBookingDto(flightsMapper.toDto(flight), seatsMapper.toDtoList(seats));
            bookings.add(bookingDto);
        }
        return ResponseEntity.ok(bookings);
    }
}

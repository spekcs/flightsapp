package com.example.flightsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookedSeatRepository extends JpaRepository<BookedSeatEntity, Long> {
    List<BookedSeatEntity> findByFlightId(Long id);
    List<BookedSeatEntity> findAllByBooking(BookingEntity booking);
}

package com.example.flightsapp.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "booked_seats", schema = "public")
public class BookedSeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booked_seat_id")
    private long bookedSeatId;
    @Column(name = "seat_code")
    private String seatCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private BookingEntity booking;
}

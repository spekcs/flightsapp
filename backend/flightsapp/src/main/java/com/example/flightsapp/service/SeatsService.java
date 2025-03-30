package com.example.flightsapp.service;

import com.example.flightsapp.controller.dto.SeatDto;
import com.example.flightsapp.controller.dto.SeatRecommendationDto;
import com.example.flightsapp.exception.ApplicationException;
import com.example.flightsapp.exception.NotFoundException;
import com.example.flightsapp.mapping.SeatsMapper;
import com.example.flightsapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatsService {
    private static final Integer RECOMMENDATION_SCORE = 1000;

    private static final Double RANDOM_BOOKED_WEIGHT = 0.4;

    private static final List<String> seatCodes = List.of(
            "1A", "1B", "1C", "1D", "2A", "2B", "2C", "2D", "3A", "3B", "3C", "3D",
            "4A", "4B", "4C", "4D", "5A", "5B", "5C", "5D", "6A", "6B", "6C", "6D",
            "7A", "7B", "7C", "7D", "8A", "8B", "8C", "8D", "9A", "9B", "9C", "9D",
            "10A", "10B", "10C", "10D", "11A", "11B", "11C", "11D", "12A", "12B", "12C", "12D",
            "13A", "13B", "13C", "13D", "14A", "14B", "14C", "14D", "15A", "15B", "15C", "15D",
            "16A", "16B", "16C", "16D", "17A", "17B", "17C", "17D", "18A", "18B", "18C", "18D",
            "19A", "19B", "19C", "19D", "20A", "20B", "20C", "20D", "21A", "21B", "21C", "21D",
            "22A", "22B", "22C", "22D", "23A", "23B", "23C", "23D", "24A", "24B", "24C", "24D",
            "25A", "25B", "25C", "25D", "26A", "26B", "26C", "26D", "27A", "27B", "27C", "27D",
            "28A", "28B", "28C", "28D", "29A", "29B", "29C", "29D", "30A", "30B", "30C", "30D",
            "31A", "31B", "31C", "31D", "32A", "32B", "32C", "32D", "33A", "33B", "33C", "33D"
    );

    private static final List<Integer> legRoomRows = List.of(1, 13, 14);
    private final BookedSeatRepository bookedSeatRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SeatsMapper seatsMapper;


    public ResponseEntity<List<SeatDto>> getSeats(Long flightId) {
        Optional<FlightEntity> flightEntity = flightRepository.findById(flightId);
        if (flightEntity.isEmpty()) {
            throw new NotFoundException(String.format("Flight with id %d not found.", flightId));
        }
        List<BookedSeatEntity> bookedSeats = bookedSeatRepository.findByFlightId(flightId);

        if (bookedSeats.isEmpty()) {
            bookedSeats.addAll(bookDummySeats(flightId));
        }
        return ResponseEntity.ok(seatsMapper.toDtoList(bookedSeats));
    }

    private List<BookedSeatEntity> bookDummySeats(Long flightId) {
        BookingEntity bookingEntity = new BookingEntity();
        Optional<FlightEntity> flightEntity = flightRepository.findById(flightId);
        assert flightEntity.isPresent();
        bookingEntity.setFlight(flightEntity.get());
        Optional<UserEntity> adminUser = userRepository.findById(0L);
        assert adminUser.isPresent();
        bookingEntity.setUser(adminUser.get());

        bookingRepository.save(bookingEntity);

        List<BookedSeatEntity> seats = new ArrayList<>();

        for (String seatCode : seatCodes) {
            if (getWeightedBool()) {
                BookedSeatEntity seat = new BookedSeatEntity();
                seat.setSeatCode(seatCode);
                seat.setFlight(flightEntity.get());
                seat.setBooking(bookingEntity);
                bookedSeatRepository.save(seat);
                seats.add(seat);
            }
        }
        return seats;
    }

    private boolean getWeightedBool() {
        return ThreadLocalRandom.current().nextDouble() < SeatsService.RANDOM_BOOKED_WEIGHT;
    }


    public ResponseEntity<List<SeatDto>> getRecommendation(Long flightId, SeatRecommendationDto seatRecommendationDto) {
        List<String> availableSeats = new ArrayList<>(seatCodes);
        for (BookedSeatEntity seat : bookedSeatRepository.findByFlightId(flightId)) {
            availableSeats.remove(seat.getSeatCode());
        }

        List<SeatWithRecommendation> recommendations = new ArrayList<>();
        for (String seatCode : availableSeats) {
            recommendations.add(new SeatWithRecommendation(seatCode, 0));
        }

        Long count = seatRecommendationDto.count();
        if (count > availableSeats.size()) {
            throw new ApplicationException("Not enough available seats for recommendation.");
        }

        switch (seatRecommendationDto.recommendBy()) {
            case TOGETHER -> {
                recommendTogether(recommendations);
            }
            case WINDOW -> {
                recommendWindow(recommendations);
            }
            case EXIT -> {
                recommendExit(recommendations);
            }
            case LEGROOM -> {
                recommendLegroom(recommendations);
            }
        }

        List<SeatWithRecommendation> bestSeats = recommendations.stream().sorted(Comparator.reverseOrder()).limit(count).toList();
        List<SeatDto> seatDtos = new ArrayList<>();
        for (SeatWithRecommendation seat : bestSeats) {
            seatDtos.add(new SeatDto(seat.getSeatCode()));
        }
        return ResponseEntity.ok(seatDtos);
    }

    private static final Map<String, Integer> rows = Map.of("A", 1, "B", 2, "C", 3, "D", 4);

    private void recommendTogether(List<SeatWithRecommendation> seats) {
        for (SeatWithRecommendation seat : seats) {
            String[] codeDecomp = seat.getSeatCode().split("(?=[A-Z])");
            int row = Integer.parseInt(codeDecomp[0]);
            int seatNumber = rows.get(codeDecomp[1]);
            seat.setScore(row * 10 + seatNumber);
        }
    }

    private void recommendExit(List<SeatWithRecommendation> seats) {
        for (SeatWithRecommendation seat : seats) {
            int row = Integer.parseInt(seat.getSeatCode().split("(?=[A-Z])")[0]);
            Integer score = 70 - Math.min(row, Math.min(Math.abs(13 - row), Math.min(Math.abs(14 - row), Math.abs(33 - row))));
            if (seat.getSeatCode().startsWith("B") || seat.getSeatCode().startsWith("C")) {
                score++;
            }
            seat.setScore(score);
        }
    }

    private void recommendWindow(List<SeatWithRecommendation> seats) {
        for (SeatWithRecommendation seat : seats) {
            if (seat.seatCode.startsWith("A") || seat.seatCode.startsWith("D")) {
                seat.setScore(seat.getScore() + RECOMMENDATION_SCORE);
            }
        }
    }


    private void recommendLegroom(List<SeatWithRecommendation> seats) {
        for (SeatWithRecommendation seat : seats) {
            int row = Integer.parseInt(seat.getSeatCode().split("(?=[A-Z])")[0]);
            if (legRoomRows.contains(row)) {
                seat.setScore(seat.getScore() + RECOMMENDATION_SCORE);
            }
            if (seat.seatCode.startsWith("A") || seat.seatCode.startsWith("D")) {
                seat.setScore(seat.getScore() + 1);
            }
        }
    }

    @AllArgsConstructor
    @Getter @Setter
    private static class SeatWithRecommendation implements Comparable<SeatWithRecommendation> {
        private String seatCode;
        private Integer score;

        @Override
        public int compareTo(SeatWithRecommendation seatWithRecommendation) {
            return this.score.compareTo(seatWithRecommendation.score);
        }
    }
}

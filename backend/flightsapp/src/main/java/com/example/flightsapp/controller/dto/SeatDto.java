package com.example.flightsapp.controller.dto;


import java.util.List;

public record SeatDto(
        String seatCode
){
    public enum Type {
        BUSINESS,
        ECONOMY;

        private static final List<String> businessClass = List.of("1A", "1B", "1C", "1D",
                "2A", "2B", "2C", "2D", "3A", "3B", "3C", "3D", "4A", "4B", "4C", "4D");

        public static Type getType(String seatCode) {
            if (businessClass.contains(seatCode)) {
                return Type.BUSINESS;
            }
            return Type.ECONOMY;
        }
    }
}

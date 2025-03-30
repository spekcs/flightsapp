package com.example.flightsapp.controller.dto;

public record SeatRecommendationDto (
        Long count,
        Type recommendBy
) {
    public enum Type {
        WINDOW,
        LEGROOM,
        EXIT,
        TOGETHER
    }
}


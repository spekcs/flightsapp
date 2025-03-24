package com.example.flightsapp.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class ErrorResponse {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}

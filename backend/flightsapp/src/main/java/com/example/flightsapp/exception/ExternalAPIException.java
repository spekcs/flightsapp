package com.example.flightsapp.exception;

public class ExternalAPIException extends RuntimeException {
    public ExternalAPIException(String message) {
        super(message);
    }
}

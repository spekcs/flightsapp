package com.example.flightsapp.controller.dto;

import lombok.Data;

@Data
public class FlightsAPIPagination {
    private int count;
    private int limit;
    private int offset;
    private int total;
}

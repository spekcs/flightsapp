package com.example.flightsapp.controller.dto;

import lombok.Data;

import java.util.List;


@Data
public class FlightsAPIResponseObject {
    private List<FlightsAPIFlight> data;
    private int error;
    private FlightsAPIPagination pagination;

}

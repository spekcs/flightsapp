package com.example.flightsapp.controller.dto;

import java.util.List;

public record PageResponse<T> (
        List<T> content,
        Integer pageOffset,
        Integer limit,
        Integer count,
        Integer total
) {
}

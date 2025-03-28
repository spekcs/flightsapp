package com.example.flightsapp.controller;

import com.example.flightsapp.controller.dto.LoginResponseDto;
import com.example.flightsapp.controller.dto.UserAuthDto;
import com.example.flightsapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserAuthDto userAuthDto) {
        return userService.login(userAuthDto);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody UserAuthDto userAuthDto) {
        return userService.register(userAuthDto);
    }

}

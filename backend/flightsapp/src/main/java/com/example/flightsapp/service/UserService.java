package com.example.flightsapp.service;

import com.example.flightsapp.controller.dto.LoginResponseDto;
import com.example.flightsapp.controller.dto.UserAuthDto;
import com.example.flightsapp.exception.ApplicationException;
import com.example.flightsapp.exception.NotFoundException;
import com.example.flightsapp.repository.UserEntity;
import com.example.flightsapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserIdService userIdService;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<LoginResponseDto> register(UserAuthDto userAuthDto) {
        if (userAuthDto.username() == null || userAuthDto.password() == null) {
            throw new ApplicationException("Username and password are required");
        }

        if (userRepository.existsByUsername(userAuthDto.username())) {
            throw new ApplicationException("Username is already taken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(userAuthDto.username());
        user.setPassword(passwordEncoder.encode(userAuthDto.password()));
        userRepository.save(user);
        return ResponseEntity.ok(new LoginResponseDto(userIdService.generateToken(user)));
    }

    public ResponseEntity<LoginResponseDto> login(UserAuthDto userAuthDto) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(userAuthDto.username());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User with this username and password combination not found");
        }
        UserEntity user = optionalUser.get();
        if (!passwordEncoder.matches(userAuthDto.password(), user.getPassword())) {
            throw new NotFoundException("User with this username and password combination not found");
        }
        String token = userIdService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}

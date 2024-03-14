package com.bcd.big_cheese_delivery.web.controller;

import com.bcd.big_cheese_delivery.service.UserService;
import com.bcd.big_cheese_delivery.web.dto.Credentials;
import com.bcd.big_cheese_delivery.web.dto.JwtToken;
import com.bcd.big_cheese_delivery.web.dto.UserCreationDto;
import com.bcd.big_cheese_delivery.web.dto.UserDto;
import com.bcd.big_cheese_delivery.web.mapper.JwtTokenMapper;
import com.bcd.big_cheese_delivery.web.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenMapper jwtTokenMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid UserCreationDto userDto){
        var newUser = userService.create(userMapper.toEntity(userDto));
        return new ResponseEntity<>(userMapper.toPayload(newUser), HttpStatus.CREATED);
    }
    @PostMapping("/sign-in")
    public ResponseEntity<JwtToken> signIn(@RequestBody @Valid Credentials credentials) {
        return ResponseEntity.of(userService
                .signIn(credentials.getUsername(), credentials.getPassword())
                .map(jwtTokenMapper::toPayload));
    }
}

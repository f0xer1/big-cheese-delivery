package com.bcd.big_cheese_delivery.web.controller;
import static com.bcd.big_cheese_delivery.web.WebConstants.*;
import com.bcd.big_cheese_delivery.service.UserService;
import com.bcd.big_cheese_delivery.web.dto.auth.Credentials;
import com.bcd.big_cheese_delivery.web.dto.auth.JwtToken;
import com.bcd.big_cheese_delivery.web.dto.user.UserCreationDto;
import com.bcd.big_cheese_delivery.web.dto.user.UserDto;
import com.bcd.big_cheese_delivery.web.mapper.JwtTokenMapper;
import com.bcd.big_cheese_delivery.web.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api"+ AUTH, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenMapper jwtTokenMapper;

    @PostMapping(SIGN_UP)
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid UserCreationDto userDto){
        var newUser = userService.create(userMapper.toEntity(userDto));
        return new ResponseEntity<>(userMapper.toPayload(newUser), HttpStatus.CREATED);
    }
    @PostMapping(SIGN_IN)
    public ResponseEntity<JwtToken> signIn(@RequestBody @Valid Credentials credentials) {
        return ResponseEntity.of(userService
                .signIn(credentials.getUsername(), credentials.getPassword())
                .map(jwtTokenMapper::toPayload));
    }
}

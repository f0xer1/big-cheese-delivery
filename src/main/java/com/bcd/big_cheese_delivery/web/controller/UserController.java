package com.bcd.big_cheese_delivery.web.controller;

import com.bcd.big_cheese_delivery.service.UserService;
import com.bcd.big_cheese_delivery.web.dto.user.UserDto;
import com.bcd.big_cheese_delivery.web.dto.user.UserUpdateDto;
import com.bcd.big_cheese_delivery.web.mapper.UserMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@SecurityRequirement(name = "bearer_token")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/self")
    public ResponseEntity<UserDto> getSelf(Principal principal){
        return ResponseEntity.of(userService.findByUsername(principal.getName())
                .map(userMapper::toPayload));
    }

    @PatchMapping("/self")
    public ResponseEntity<UserDto> updateSelf(Principal principal, @Valid @RequestBody UserUpdateDto userUpdateDto){
        return ResponseEntity.of(userService.findByUsername(principal.getName())
                .map(user -> userMapper.partialUpdate(userUpdateDto, user))
                .map(userService::update)
                .map(userMapper::toPayload));
    }
}

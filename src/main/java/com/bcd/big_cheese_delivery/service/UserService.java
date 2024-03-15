package com.bcd.big_cheese_delivery.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bcd.big_cheese_delivery.domain.User;

import java.util.Optional;

public interface UserService {
    User create(User user);

    Optional<DecodedJWT> signIn(String username, String password);

    Optional<User> findByUsername(String username);

    User update(User user);
}

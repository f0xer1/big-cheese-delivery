package com.bcd.big_cheese_delivery.service;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.bcd.big_cheese_delivery.domain.User;
import com.bcd.big_cheese_delivery.exception.UserAlreadyExistsException;
import com.bcd.big_cheese_delivery.repository.UserRepository;
import com.bcd.big_cheese_delivery.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
        throw new UserAlreadyExistsException(
                "Username %s is already in use".formatted(user.getUsername()));
    }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }
    @Override
    public Optional<DecodedJWT> signIn(String username, String password) {
        if (!existsByCredentials(username, password)) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
        return jwtTokenProvider.toDecodedJWT(jwtTokenProvider.generateToken(username));
    }
    private Optional<User> findByCredentials(String username, String password) {
        var maybeUser = repository.findByUsername(username);
        if (maybeUser.isPresent()) {
            if (passwordEncoder.matches(password, maybeUser.get().getPassword())) {
                return maybeUser;
            }
        }
        return Optional.empty();
    }

    private boolean existsByCredentials(String username, String password) {
        return findByCredentials(username, password).isPresent();
    }
}

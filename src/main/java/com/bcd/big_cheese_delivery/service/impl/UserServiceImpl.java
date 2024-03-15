package com.bcd.big_cheese_delivery.service.impl;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.bcd.big_cheese_delivery.domain.User;
import com.bcd.big_cheese_delivery.exception.UserAlreadyExistsException;
import com.bcd.big_cheese_delivery.repository.UserRepository;
import com.bcd.big_cheese_delivery.security.JwtTokenProvider;
import com.bcd.big_cheese_delivery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public User update(User user) {
        checkIfUserAlreadyExists(user);
        return repository.save(user);
    }

    @Override
    @Transactional
    public User create(User user) {
        checkIfUserAlreadyExists(user);
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

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
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

    private void checkIfUserAlreadyExists(User user){
        if (isEmailInUse(user)) {
            throw new UserAlreadyExistsException(
                    "Email %s is already in use".formatted(user.getEmail()));
        }
        if (isUsernameInUse(user)){
            throw new UserAlreadyExistsException(
                    "Username %s is already in use".formatted(user.getUsername()));
        }
    }

    private boolean isUsernameInUse(User user){
        return repository.findByUsername(user.getUsername())
                .filter(founded -> !Objects.equals(founded.getId(), user.getId())).isPresent();
    }
    private boolean isEmailInUse(User user){
        return repository.findByEmail(user.getEmail())
                .filter(founded -> !Objects.equals(founded.getId(), user.getId())).isPresent();

    }


    private boolean existsByCredentials(String username, String password) {
        return findByCredentials(username, password).isPresent();
    }
}

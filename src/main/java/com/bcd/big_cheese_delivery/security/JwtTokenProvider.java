package com.bcd.big_cheese_delivery.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenProvider {
  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.issuer}")
  private String jwtIssuer;

  @Value("${jwt.expire.days}")
  private Integer expireDays;

  public String generateToken(String username) {
    return JWT.create()
        .withIssuer(jwtIssuer)
        .withSubject(username)
        .withExpiresAt(LocalDate.now()
            .plusDays(expireDays)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant())
        .sign(Algorithm.HMAC256(jwtSecret));
  }

  public Optional<DecodedJWT> toDecodedJWT(String token) throws JWTVerificationException {
    return Optional.of(JWT.require(Algorithm.HMAC256(jwtSecret))
        .withIssuer(jwtIssuer)
        .build()
        .verify(token));
  }

  public String getUsernameFromToken(String token) {
    return JWT.require(Algorithm.HMAC256(jwtSecret))
        .withIssuer(jwtIssuer)
        .build()
        .verify(token)
        .getSubject();
  }
}

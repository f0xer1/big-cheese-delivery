package com.bcd.big_cheese_delivery.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtProvider;
  private final UserDetailsService userDetailsService;
  private final HandlerExceptionResolver resolver;

  public JwtFilter(JwtTokenProvider jwtProvider,
                   UserDetailsService userDetailsService,
                   @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
    this.jwtProvider = jwtProvider;
    this.userDetailsService = userDetailsService;
    this.resolver = handlerExceptionResolver;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    var maybeToken = getTokenFromRequest(request);
    if (maybeToken.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      var token = maybeToken.get();
      jwtProvider.toDecodedJWT(token).ifPresent(decodedJWT -> {
        var username = jwtProvider.getUsernameFromToken(token);
        var userDetails = userDetailsService.loadUserByUsername(username);
        var authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
      });
    } catch (JWTVerificationException exception) {
      resolver.resolveException(request, response, null, exception);
      return;
    }
    filterChain.doFilter(request, response);
  }

  private Optional<String> getTokenFromRequest(HttpServletRequest request) {
    var bearer = request.getHeader("Authorization");
    if (hasText(bearer) && bearer.startsWith("Bearer ")) {
      return Optional.of(bearer.substring(7));
    }
    return Optional.empty();
  }
}

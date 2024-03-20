package com.bcd.big_cheese_delivery.web.dto.auth;

import lombok.Data;

@Data
public class JwtToken {
  private String token;
  private String type;
  private String algorithm;
  private String expiresAt;
}

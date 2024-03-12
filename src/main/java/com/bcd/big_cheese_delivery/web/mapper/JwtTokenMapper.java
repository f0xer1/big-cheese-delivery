package com.bcd.big_cheese_delivery.web.mapper;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.bcd.big_cheese_delivery.web.dto.JwtToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JwtTokenMapper {

    JwtToken toPayload(DecodedJWT jwt);
}

package com.bcd.big_cheese_delivery.web.mapper;

import com.bcd.big_cheese_delivery.domain.User;
import com.bcd.big_cheese_delivery.web.dto.UserCreationDto;
import com.bcd.big_cheese_delivery.web.dto.UserDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toPayload(User user);


    User toEntity(UserCreationDto userDto);


}
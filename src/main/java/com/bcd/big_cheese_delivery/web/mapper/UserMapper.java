package com.bcd.big_cheese_delivery.web.mapper;

import com.bcd.big_cheese_delivery.domain.User;
import com.bcd.big_cheese_delivery.web.dto.user.UserCreationDto;
import com.bcd.big_cheese_delivery.web.dto.user.UserDto;
import com.bcd.big_cheese_delivery.web.dto.user.UserUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toPayload(User user);

  User toEntity(UserCreationDto userDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User partialUpdate(UserUpdateDto userUpdateDto, @MappingTarget User user);
}

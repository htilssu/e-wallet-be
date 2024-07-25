package com.ewallet.ewallet.dto.mapper;

import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.models.UserDto;
import org.aspectj.lang.annotation.After;
import org.mapstruct.*;

import java.time.format.DateTimeFormatter;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING, uses = {PartnerMapper.class})
public interface UserMapper {

    User toEntity(UserDto userDto);
    UserDto toDto(User user);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(
            UserDto userDto,
            @MappingTarget User user);

    @AfterMapping
    default void create(@MappingTarget UserDto userDto, User user) {
        userDto.setCreated(DateTimeFormatter.ISO_DATE_TIME.format(user.getCreated()));
        userDto.setDob(DateTimeFormatter.ISO_DATE_TIME.format(user.getDob()));
    }
}
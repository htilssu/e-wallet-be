package com.ewallet.ewallet.models;

import com.ewallet.ewallet.dto.response.AtmCardDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface AtmCardMapper {

    AtmCard toEntity(AtmCardDto atmCardDto);

    AtmCardDto toDto(AtmCard atmCard);
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AtmCard partialUpdate(
            AtmCardDto atmCardDto,
            @MappingTarget AtmCard atmCard);
}
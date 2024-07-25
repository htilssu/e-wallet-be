package com.ewallet.ewallet.dto.mapper;

import com.ewallet.ewallet.dto.response.PartnerDto;
import com.ewallet.ewallet.dto.response.PartnerRequest;
import com.ewallet.ewallet.models.Partner;
import org.mapstruct.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.format.DateTimeFormatter;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartnerMapper {

    Partner toEntity(PartnerDto partnerDto);
    Partner toEntity(PartnerRequest partnerRequest);

    @Mapping(target = "created", ignore = true)
    PartnerDto toDto(Partner partner);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Partner partialUpdate(
            PartnerDto partnerDto,
            @MappingTarget Partner partner);

    @AfterMapping
    default void create(@MappingTarget PartnerDto partnerDto, Partner partner) {
        if (partner.getCreated() != null) {
            partnerDto.setCreated(DateTimeFormatter.ISO_DATE_TIME.format(partner.getCreated().atStartOfDay()));
        }

    }
}
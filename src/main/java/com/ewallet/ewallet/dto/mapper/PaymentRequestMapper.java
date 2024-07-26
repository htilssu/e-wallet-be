package com.ewallet.ewallet.dto.mapper;

import com.ewallet.ewallet.dto.response.PaymentRequestDto;
import com.ewallet.ewallet.models.PaymentRequest;
import com.ewallet.ewallet.payment.request.PaymentRequestData;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentRequestMapper {

    PaymentRequest toEntity(PaymentRequestDto paymentRequestDto);

    @Mappings({
            @Mapping(target = "created", source = "created", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "updated", source = "updated", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    PaymentRequestDto toDto(PaymentRequest paymentRequest);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PaymentRequest partialUpdate(
            PaymentRequestDto paymentRequestDto,
            @MappingTarget PaymentRequest paymentRequest);

    PaymentRequest toEntity(PaymentRequestData paymentRequestData);
}
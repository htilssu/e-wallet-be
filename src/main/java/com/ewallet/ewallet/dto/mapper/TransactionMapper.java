package com.ewallet.ewallet.dto.mapper;

import com.ewallet.ewallet.dto.request.TransactionRequest;
import com.ewallet.ewallet.dto.response.TransactionResponse;
import com.ewallet.ewallet.models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    @Mappings({
            @Mapping(target =
                             "id", ignore = true),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "updated", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    Transaction toEntity(TransactionRequest transactionRequest);

    @Mappings({
            @Mapping(target = "created", source = "created"),
            @Mapping(target = "updated", source = "updated")
    })
    TransactionResponse toResponse(Transaction transaction);
}

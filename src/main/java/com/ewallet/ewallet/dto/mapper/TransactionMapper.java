package com.ewallet.ewallet.dto.mapper;

import com.ewallet.ewallet.dto.request.TransactionRequest;
import com.ewallet.ewallet.dto.response.TransactionResponse;
import com.ewallet.ewallet.models.Partner;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.partner.PartnerRepository;
import com.ewallet.ewallet.user.UserRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.*;

import java.util.List;
import java.util.Optional;

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

    List<TransactionResponse> toListDto(List<Transaction> transactionList);


}

package com.ewallet.ewallet.dto.mapper;

import com.ewallet.ewallet.dto.response.WalletTransactionResponse;
import com.ewallet.ewallet.models.WalletTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletTransactionMapper {

    WalletTransactionResponse toResponse(WalletTransaction walletTransaction);

}

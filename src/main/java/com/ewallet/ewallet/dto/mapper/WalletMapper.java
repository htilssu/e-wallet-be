package com.ewallet.ewallet.dto.mapper;

import com.ewallet.ewallet.dto.response.WalletResponse;
import com.ewallet.ewallet.models.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponse toResponse(Wallet wallet);
}

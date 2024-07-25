package com.ewallet.ewallet.dto.response;

import com.ewallet.ewallet.wallet.Wallet;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class WalletTransactionResponse {
    WalletResponse senderWallet;
    WalletResponse receiverWallet;
    private String id;
    private int senderWalletId;
    private int receiverWalletId;
}

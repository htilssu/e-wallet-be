package com.ewallet.ewallet.dto.request;

import lombok.Data;

@Data
public class TransactionRequest {
    /**
     * Loại ví nhận tiền
     */
    String transactionTarget;
    /**
     * Người nhận tiền
     */
    String sendTo;
    double money;
    String currency;
    /**
     * Loại giao dịch ví dụ như nạp tiền {@code topup}, chuyển tiền {@code transfer},
     * rút tiền {@code withdraw}
     */
    String transactionType;
}

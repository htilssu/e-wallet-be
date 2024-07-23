package com.ewallet.ewallet.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class TransferData {

    /**
     * Loại ví nhận tiền
     */
    String transferTarget;
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

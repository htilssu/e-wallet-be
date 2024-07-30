package com.ewallet.ewallet.payment.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentRequestData {

    double money;
    String voucherId;
    String voucherName;
    String voucherCode;
    double voucherDiscount;
    String returnUrl;
    String cancelUrl;
    String success;
    String successUrl;
    String orderId;
}

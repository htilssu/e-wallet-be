package com.ewallet.ewallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponse {

     double money;
     String currency;
     String transactionType;
     String transactionTarget;
     String status;
     String created;
     String updated;
     String id;
}

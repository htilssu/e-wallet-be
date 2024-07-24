package com.ewallet.ewallet.transaction;

import com.ewallet.ewallet.util.ObjectUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransactionDetail {

    Transaction transaction;
    WalletTransaction walletTransaction;


    public ObjectNode flatten() {
       return ObjectUtil.mergeObjects(transaction, walletTransaction);
    }

}

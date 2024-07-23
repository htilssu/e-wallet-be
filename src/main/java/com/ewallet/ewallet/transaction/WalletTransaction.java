package com.ewallet.ewallet.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@AllArgsConstructor
@Data
public class WalletTransaction {

    @Id
  private  String id;
  private  int senderWallet;
  private  int receiverWallet;

}

package com.ewallet.ewallet.wallet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@Table("wallet")
public class Wallet {
    @Id
    private int id;
    private String ownerId;
    private String ownerType;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private double balance;
    private String currency;

    public void sendMoneyTo(Wallet receiverWallet, double money) {
        this.balance -= money;
        receiverWallet.addBalance(money);    }

    @JsonIgnoreProperties
    public void addBalance(double money) {
        this.balance += money;
    }
}

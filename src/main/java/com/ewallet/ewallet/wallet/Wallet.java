package com.ewallet.ewallet.wallet;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Wallet {


    @Id
    private int id;
    private String ownerId;
    private String ownerType;
    private double balance;
    private String currency;

    public void sendMoneyTo(Wallet receiverWallet, double money) {
        this.balance -= money;
        receiverWallet.addBalance(money);
    }

    public void addBalance(double money) {
        this.balance += money;
    }

}

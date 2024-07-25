package com.ewallet.ewallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction {

    @Id
    @Size(max = 15)
    @Column(name = "id", nullable = false, length = 15)
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Transaction transaction;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('wallet_transaction_sender_wallet_seq'::regclass)")
    @JoinColumn(name = "sender_wallet", nullable = false)
    private Wallet senderWallet;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("nextval('wallet_transaction_receiver_wallet_seq'::regclass)")
    @JoinColumn(name = "receiver_wallet", nullable = false)
    private Wallet receiverWallet;

}
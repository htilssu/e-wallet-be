package com.ewallet.ewallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "\"order\"", uniqueConstraints = {
        @UniqueConstraint(name = "order_id_partner_id_key", columnNames = {"id", "partner_id"}),
        @UniqueConstraint(name = "order_transaction_id_key", columnNames = {"transaction_id"}),
        @UniqueConstraint(name = "order_external_transaction_id_key",
                          columnNames = {"external_transaction_id"})
})
public class Order {

    @Id
    @Size(max = 15)
    @Column(name = "id", nullable = false, length = 15)
    private String id;

    @Size(max = 10)
    @Column(name = "partner_id", length = 10)
    private String partnerId;

    @NotNull
    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    private BigDecimal money;

    @Size(max = 50)
    @NotNull
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Size(max = 15)
    @Column(name = "transaction_id", length = 15)
    private String transactionId;

    @Size(max = 15)
    @Column(name = "voucher_id", length = 15)
    private String voucherId;

    @NotNull
    @ColumnDefault("nextval('order_external_transaction_id_seq'::regclass)")
    @Column(name = "external_transaction_id", nullable = false)
    private Integer externalTransactionId;

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "created", nullable = false)
    private LocalDate created;

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "updated", nullable = false)
    private LocalDate updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method")
    private PaymentMethod paymentMethod;

}
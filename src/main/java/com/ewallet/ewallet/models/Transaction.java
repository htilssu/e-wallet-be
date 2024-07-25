package com.ewallet.ewallet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @Size(max = 15)
    @ColumnDefault("generate_transaction_id()")
    @Column(name = "id", nullable = false, length = 15)
    private String id;

    @NotNull
    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    private BigDecimal money;

    @Size(max = 3)
    @NotNull
    @ColumnDefault("'VND'")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'transfer'")
    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'wallet'")
    @Column(name = "transaction_target", nullable = false, length = 20)
    private String transactionTarget;

    @Size(max = 50)
    @NotNull
    @ColumnDefault("'pending'")
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "updated", nullable = false)
    private Instant updated;

}
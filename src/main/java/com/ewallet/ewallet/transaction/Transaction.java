package com.ewallet.ewallet.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Locale;

@AllArgsConstructor
@Getter
@Table("transaction")
public class Transaction {

    private double money;
    private String currency;
    private String transactionType;
    private String transactionTarget;
    private String status;
    private LocalDateTime created;
    private LocalDateTime updated;
    @Id
    private String id;
}

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

@Getter
@Setter
@Entity
@Table(name = "payment_system")
public class PaymentSystem {

    @Id
    @ColumnDefault("nextval('payment_system_id_seq'::regclass)")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "api_key")
    private String apiKey;

    @Size(max = 255)
    @Column(name = "api_secret")
    private String apiSecret;
    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;

/*
 TODO [Reverse Engineering] create field to map the 'type' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "type", columnDefinition = "payment_system_type not null")
    private Object type;
*/
}
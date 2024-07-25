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
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "partner")
public class Partner {

    @Id
    @Size(max = 10)
    @ColumnDefault("generate_partner_id()")
    @Column(name = "id", nullable = false, length = 10)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 255)
    @NotNull
    @Column(name = "api_base_url", nullable = false)
    private String apiBaseUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @NotNull
    @Column(name = "balance", nullable = false)
    private double balance;

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "created", nullable = false)
    private LocalDate created;

}
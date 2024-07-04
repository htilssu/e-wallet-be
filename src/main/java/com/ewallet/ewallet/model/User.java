package com.ewallet.ewallet.model;

import com.ewallet.ewallet.account.Account;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


public class User extends Account {
    private double money;
    private String gender;
    private String address;
    private String phone;
    private String role;
}

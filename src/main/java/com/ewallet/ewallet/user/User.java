package com.ewallet.ewallet.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Table("\"user\"")
public class User {

    @Id
    String id;
    String userName;
    String firstName;
    String lastName;
    String email;
    String password;
    String phoneNumber;
    String address;
    boolean isActive;
    boolean isVerified;
    LocalDate dob;
    boolean gender;
    LocalDate created;
    String job;
}

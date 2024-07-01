package com.ewallet.ewallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private String role;

}

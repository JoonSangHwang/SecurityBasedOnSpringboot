package com.joonsang.example.Authentication_Ajax.dto;

import lombok.Data;

@Data
public class AccountDto {
    private String username;
    private String password;
    private String email;
    private String age;
    private String role;
}

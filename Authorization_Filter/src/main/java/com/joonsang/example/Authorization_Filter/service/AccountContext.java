package com.joonsang.example.Authorization_Filter.service;

import com.joonsang.example.Authorization_Filter.domain.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AccountContext extends User {

    private final Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        // 스프링 시큐리티가 제공하는 User 객체를 상속하여 UserDetails 객체를 반환
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}

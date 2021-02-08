package com.joonsang.example.SecurityExam.account;

import com.joonsang.example.SecurityExam.entity.Account;

public class AccountContext {

    private static final ThreadLocal<Account> ACCOUNT_THREAD_LOCAL = new ThreadLocal<>();

    public static void setAccountThreadLocal(Account account) {
        ACCOUNT_THREAD_LOCAL.set(account);
    }

    public static Account getAccountTreadLocal() {
        return ACCOUNT_THREAD_LOCAL.get();
    }
}

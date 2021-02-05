package com.joonsang.example.SecurityExam.account;

import com.joonsang.example.SecurityExam.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    /**
     * UserDetailsService 인터페이스는 사용자가 입력한 username 을 load 하여
     * DB 에서 찾은 후, UserDetails 타입으로 반환 하는 역할
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }

        // Account 타입을 UserDetails 으로 캐스팅 하기 위해, 스프링 시큐리티에서 User 객체를 지원
        return User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    /**
     * 새로운 계정 생성
     */
    public Account createNewAccount(Account account) {

        if (
            account.getRole() == null       ||
            account.getUsername() == null   ||
            account.getPassword() == null
        ) {
            throw new NullPointerException();
        }

        /**
         * 패스워드 인코더 [하드코딩]
         * - 스프링 시큐리티는 특정한 패스워드 패턴을 요구한다. ex) {noop}password
         */
        account.setPassword("{noop}" + account.getPassword());
        // 저장 후, Json 으로 리턴
        return accountRepository.save(account);
    }
}

package com.joonsang.example.Authorization_Filter.service;

import com.joonsang.example.Authorization_Filter.domain.entity.Account;
import com.joonsang.example.Authorization_Filter.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 로그인 요청
     * - ID 를 DB 에서 조회 후 권한 생성 및 유저 정보 반환
     * - AccountContext 클래스에서 UserDetails 객체를 반환 하도록 함
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = userRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        // 권한 정보 생성
        List<GrantedAuthority> roles = new ArrayList<>();
//        roles.add(new SimpleGrantedAuthority(account.getUserRoles()));

        // 유저 정보 (UserDetails 인터페이스를 사용하여 시큐리티가 구현 된 User 객체를 사용할 수 있다.)
        AccountContext accountContext = new AccountContext(account, roles);

        return accountContext;
    }
}

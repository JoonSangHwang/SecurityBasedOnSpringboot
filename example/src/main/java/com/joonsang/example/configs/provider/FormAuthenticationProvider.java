package com.joonsang.example.configs.provider;

import com.joonsang.example.configs.common.FormWebAuthenticationDetails;
import com.joonsang.example.service.AccountContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;

@Slf4j
public class FormAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public FormAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 인증 검증
     *
     * @param authentication    AuthenticationManager 가 주는 객체로 사용자가 입력한 계정 정보가 담겨 있다.
     */
    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();                     // 아이디
        String password = (String) authentication.getCredentials();     // 패스워드

        // ID 검증 - 커스터마이징한 loadUserByUsername()
        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        // 패스워드 검증 - 입력한 패스워드와 암호화 된 패스워드 일치 검증
        if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }

        FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = formWebAuthenticationDetails.getSecretKey();
        if (secretKey == null || "secret".equals(secretKey)) {
            throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
        }

        /* 위 검증을 다 완료할 경우, Provider 객체는 토큰을 생성한다. */

        // 토큰 생성 (파라미터: 객체정보/패스워드/권한정보)
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());

        // AuthenticationManager 에게 인증 된 정보 전달
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

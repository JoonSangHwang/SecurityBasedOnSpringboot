package com.joonsang.example.SecurityExam.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(0)       // 설정 파일 초기화 순서
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 요청 인가
        http.authorizeRequests()
                .mvcMatchers("/","/info").permitAll()
                .mvcMatchers("/admin").hasRole("admin")
                .anyRequest().authenticated()   // 기타 요청은, 인증만 하면 접근 가능
        .and()
                .formLogin()                    // Form 방식
        .and()
                .httpBasic()                    // httpBasic 사용
        ;

    }
}

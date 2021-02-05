package com.joonsang.example.SecurityExam.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
                .mvcMatchers("/","/info","/account/**").permitAll()
                .mvcMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()   // 기타 요청은, 인증만 하면 접근 가능
        .and()
                .formLogin()                    // Form 방식
        .and()
                .httpBasic()                    // httpBasic 사용
        ;
    }


    // 이 메소드가 있으면 PasswordEncoder 와 함께 쓸 수 없는데... 이유가 뭘까?
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        /**
//         * 인 메모리 유저 추가
//         *
//         * Prefix
//         * - 패스워드 인코더
//         * - noop: 사용하지 않음
//         */
//        auth.inMemoryAuthentication()
//                .withUser("memberA").password("{noop}123").roles("USER").and()
//                .withUser("admin").password("{noop}admin").roles("ADMIN");
//    }
}

package com.joonsang.example.SecurityExam.security.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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


    /**
     * ignoring 설정
     * - 정적 자원 관리
     * - StaticResourceLocation (CSS / JAVA_SCRIPT / IMAGES / WEB_JARS / favicon 등) 객체는 보안 필터를 안거치도록 설정
     *
     * 참고 : permitAll() 같은 메소드는 보안 필터를 거쳐 인증을 받을 필요가 없다고 인가를 받는 것. ignoring() 은 보안필터 자체를 안거침
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 디버그 시, 정적 자원은 필터가 0개 인걸 확인 할 수 있음
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
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

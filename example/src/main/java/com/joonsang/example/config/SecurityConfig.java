package com.joonsang.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity  // 웹 보안 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()                // 요청 보안 부분
                .anyRequest().authenticated();      // 모든 요청에 인증을 받음
        http
                .formLogin()                        // Form 로그인 인증 방식
                .loginPage("/loginPage")            // 로그인 화면 (로그인 인증을 받지 않아도 되는 페이지 권한 설정 필요)
                .defaultSuccessUrl("/")             // 인증 성공 시, URL
                .failureUrl("/login")               // 인증 실패 시, URL
                .usernameParameter("user")          // 유저
                .passwordParameter("pw")            // 비밀번호
                .loginProcessingUrl("/logn_proc")   // Form 태그의 Action URL

                // 로그인 성공
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                                        HttpServletResponse httpServletResponse,
                                                        Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication : " + authentication);
                        httpServletResponse.sendRedirect("/");
                    }
                })
                // 로그인 실패
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                                        HttpServletResponse httpServletResponse,
                                                        AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("Exception : " + exception.getMessage());
                        httpServletResponse.sendRedirect("/login");
                    }
                })
                // loginPage("/loginPage") 으로 접근 시, 인증 X 설정
                .permitAll();

    }
}

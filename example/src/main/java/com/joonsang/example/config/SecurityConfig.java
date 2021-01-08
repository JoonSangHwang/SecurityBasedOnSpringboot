package com.joonsang.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity  // 웹 보안 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()                // 요청 보안 부분
                .anyRequest().authenticated();      // 모든 요청에 인증을 받음

        /**
         * 로그인
         */
        http
                .formLogin()                        // Form 로그인 인증 방식
                .loginPage("/loginPage")            // 로그인 URL (로그인 인증을 받지 않아도 되는 페이지 권한 설정 필요)
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
                .permitAll()
            .and()
                .rememberMe()
                .rememberMeParameter("remember")                    // 파라미터 네임
                .tokenValiditySeconds(3600)                         // Default 14일
                .alwaysRemember(true)                               // 리멤버가 체크 되지 않아도 항상 실행
                .userDetailsService(userDetailsService);

        http
                .sessionManagement()
                .invalidSessionUrl("/invalid")          // 세션이 유효하지 않을 때 이동 할 페이지
                .sessionFixation().changeSessionId()    // 인증마다 세션 리프레시
                .maximumSessions(1)                     // 최대 허용 가능 세션 수 , -1 : 무제한 로그인 세션 허용
                .maxSessionsPreventsLogin(true)         // 동시 로그인 차단함,  (default 세션 만료:false)
                .expiredUrl("/expired");  	            // 세션이 만료된 경우 이동 할 페이지

        /**
         * 로그아웃
         */
        http
                .logout()                                               // Form 로그아웃 인증 방식
                .logoutUrl("/logout")                                   // 로그아웃 URL
                .logoutSuccessUrl("/login")				                // 로그아웃 성공 후, 처리 URL
                .addLogoutHandler(new LogoutHandler() {                 // 로그아웃 핸들러
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        // 세션 무효화
                        HttpSession session = request.getSession();
                        session.invalidate();
                    }
                })
                .logoutSuccessHandler(new LogoutSuccessHandler() {      // 로그아웃 성공 후, 처리 Handler
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        // 로그인 페이지로 이동
                        response.sendRedirect("/login");
                    }
                })
                .deleteCookies("remember-me");		                    // 로그아웃 성공 후, 쿠키 삭제


    }
}

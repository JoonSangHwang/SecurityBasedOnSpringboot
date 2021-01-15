package com.joonsang.example.Authentication_Form.configs;


import com.joonsang.example.Authentication_Form.configs.common.FormAuthenticationDetailsSource;
import com.joonsang.example.Authentication_Form.configs.handler.*;
import com.joonsang.example.Authentication_Form.configs.provider.FormAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private FormAuthenticationDetailsSource authenticationDetailsSource;

    @Autowired
    private AuthenticationSuccessHandler formAuthenticationSuccessHandler;          // 로그인 성공 후, 핸들러

    @Autowired
    private AuthenticationFailureHandler formAuthenticationFailureHandler;          // 로그인 실패 후, 핸들러


    /**
     * 시큐리티 세팅
     *
     * 참고: CSRF 기능이 활성화 되어 있을 경우에는 POST 방식의 요청에 한해서 LogoutFilter 가 동작
     *       CSRF 기능을 비활성화 할 경우에는 GET 방식도 LogoutFilter 가 처리
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http
                .addFilterBefore(filter, CsrfFilter.class);                 // 커스텀 필터 추가 (Default: UsernamePasswordAuthenticationFilter 보다 먼저 실행된다)

        http
                .authorizeRequests()                                        //== 승인 ==
                .antMatchers("/",                             // [URL] /
                                        "/users",                           // [URL] /users
//                                        "/user/login/**",                 // [URL] /
                                        "/login*").permitAll()              // [URL] /login*
                .antMatchers("/mypage").hasRole("USER")       // [접근 권한] USER
                .antMatchers("/messages").hasRole("MANAGER")  // [접근 권한] MANAGER
                .antMatchers("/config").hasRole("ADMIN")      // [접근 권한] ADMIN
                .anyRequest().authenticated()

        .and()
                .formLogin()                                                //== 로그인 ==
                .loginPage("/login")                                        // 로그인 페이지
                .loginProcessingUrl("/login_proc")                          // Form 태그의 Action URL
                .authenticationDetailsSource(authenticationDetailsSource)   // username, password 이외에 추가 파라미터 처리
                .defaultSuccessUrl("/")                                     // 인증 성공 시, 이동 URL
                .successHandler(formAuthenticationSuccessHandler)           // 로그인 성공 후, 핸들러
                .failureHandler(formAuthenticationFailureHandler)           // 로그인 실패 후, 핸들러
                .permitAll()                                                // 로그인 화면은 접근 권한 없음
        .and()
                .exceptionHandling()                                        // 로그인 예외, 핸들러
                .accessDeniedHandler(accessDeniedHandler())                 // 인가 거부 처리 (권한을 가지지 않은 사용자가 페이지에 접근)
        ;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }

    /**
     * 정적 자원 관리
     * - StaticResourceLocation (CSS / JAVA_SCRIPT / IMAGES / WEB_JARS 등) 객체는 보안 필터를 안거치도록 설정
     *
     * 참고 : permitAll() 같은 메소드는 보안 필터를 거쳐 인증을 받을 필요가 없다고 인가를 받는 것. ignoring() 은 보안필터 자체를 안거침
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    /**
     * 로그인
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new FormAuthenticationProvider(passwordEncoder());
    }


    /**
     * 비밀번호를 안전하게 암호화 하도록 제공
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 패스워드 암호화 처리
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

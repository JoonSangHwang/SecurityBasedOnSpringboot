package com.joonsang.example.configs;


import com.joonsang.example.configs.provider.CustomAuthenticationProvider;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 시큐리티 세팅
     *
     * 참고: CSRF 기능이 활성화 되어 있을 경우에는 POST 방식의 요청에 한해서 LogoutFilter 가 동작
     *      CSRF 기능을 비활성화 할 경우에는 GET 방식도 LogoutFilter 가 처리
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http
                .addFilterBefore(filter, CsrfFilter.class);

        http
                .authorizeRequests()
                .antMatchers("/","/users").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated()
        .and()
                .formLogin()
                .loginPage("/login")                    // 로그인 페이지
                .loginProcessingUrl("/login_proc")      // Form 태그의 Action URL
                .defaultSuccessUrl("/")                 // 인증 성공 시, 이동 URL
                .permitAll()
        ;
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
        return new CustomAuthenticationProvider();
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

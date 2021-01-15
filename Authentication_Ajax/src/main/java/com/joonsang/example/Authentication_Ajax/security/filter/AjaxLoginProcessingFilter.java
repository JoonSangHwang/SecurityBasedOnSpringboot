package com.joonsang.example.Authentication_Ajax.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joonsang.example.Authentication_Ajax.domain.Account;
import com.joonsang.example.Authentication_Ajax.dto.AccountDto;
import com.joonsang.example.Authentication_Ajax.security.token.AjaxAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginProcessingFilter() {
        super(new AntPathRequestMatcher("/api/login"));     // 요청 정보가 URL 과 매칭 되면 필터 발동 (1/2)
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        //= 인증 조건
        if (!isAjax(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        // ID 또는 PASSWORD 가 null 값이라면, 예외
        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if (StringUtils.isEmpty(accountDto.getUsername()) || StringUtils.isEmpty(accountDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty");
        }

        //= 인증 전, 토큰 생성
        AjaxAuthenticationToken ajaxAuthenticationToken = new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    // 요청 정보가 Ajax 라면 필터 발동 (2/2)
    private boolean isAjax(HttpServletRequest httpServletRequest) {
        return "XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-with"));
    }
}

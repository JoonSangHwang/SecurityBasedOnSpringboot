package com.joonsang.example.Authentication_Form.configs.common;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * username / password 이외에 추가 파라미터(ex: OTP 코드)를 처리하기 위한 방법
 *
 *   1. WebAuthenticationDetails 를 확장해서 추가 파라미터를 정의
 *   2. 사용자가 전달한 추가 파라미터를 저장하는 클래스
 * ┌──────────────────────────────────────────────────────────┐
 *   3. WebAuthenticationDetails 객체 생성
 * └──────────────────────────────────────────────────────────┘
 */
@Component
public class FormAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new FormWebAuthenticationDetails(request);
    }
}

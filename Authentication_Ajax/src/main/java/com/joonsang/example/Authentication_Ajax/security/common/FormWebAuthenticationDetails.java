package com.joonsang.example.Authentication_Ajax.security.common;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * username / password 이외에 추가 파라미터(ex: OTP 코드)를 처리하기 위한 방법
 *
 * ┌──────────────────────────────────────────────────────────┐
 *   1. WebAuthenticationDetails 를 확장해서 추가 파라미터를 정의
 *   2. 사용자가 전달한 추가 파라미터를 저장하는 클래스
 * └──────────────────────────────────────────────────────────┘
 *   3. WebAuthenticationDetails 객체 생성
 */
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    private final String secretKey;

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secret_key");
    }

    public String getSecretKey() {
        return secretKey;
    }
}

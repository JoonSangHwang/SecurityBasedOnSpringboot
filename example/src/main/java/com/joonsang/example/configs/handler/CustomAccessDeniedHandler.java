package com.joonsang.example.configs.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage;

    /**
     * @param accessDeniedException     인가 예외
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String deiniedUrl = errorPage + "?exception=" + accessDeniedException.getMessage();
        response.sendRedirect(deiniedUrl);


    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

}

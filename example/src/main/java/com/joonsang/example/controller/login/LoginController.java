package com.joonsang.example.controller.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "user/login/login";
    }

    /**
     * 로그아웃
     * 1. <form> 태그를 사용하여 POST 요청
     * 2. <a> 태그를 사용하여 GET 요청 (v)
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        // 인증 객체
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 세션 무효화 + 인증 객체 empty + SecurityContextHolder Clean
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/login";
    }

}

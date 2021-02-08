package com.joonsang.example.SecurityExam.hello;

import com.joonsang.example.SecurityExam.account.AccountContext;
import com.joonsang.example.SecurityExam.account.AccountRepository;
import com.joonsang.example.SecurityExam.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HelloController {

    @Autowired
    AccountRepository accountRepository;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("message", "Hello Guest");
        } else {
            // 초기 name 은 "user"
            model.addAttribute("message", "Hello " + principal.getName());
        }
        return "index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("message", "Info");
        return "info";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("message", "dashboard");

        // Thread Local 테스트
        AccountContext.setAccountThreadLocal(accountRepository.findByUsername(principal.getName()));
        Account account = AccountContext.getAccountTreadLocal();
        System.out.println("====================");
        System.out.println("계정 : " + account.getUsername() + " 로 로그인 하였습니다. [ThreadLocal]");
        return "dashboard";
    }

    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("message", "admin");
        return "admin";
    }
}

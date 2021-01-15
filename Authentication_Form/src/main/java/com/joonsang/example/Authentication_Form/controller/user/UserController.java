package com.joonsang.example.Authentication_Form.controller.user;


import com.joonsang.example.Authentication_Form.domain.Account;
import com.joonsang.example.Authentication_Form.dto.AccountDto;
import com.joonsang.example.Authentication_Form.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/mypage")
    public String myPage() throws Exception {
        return "user/mypage";
    }

    @GetMapping(value = "/users")
    public String createUser() {
        return "user/login/register";
    }

    /**
     * 회원 가입
     */
    @PostMapping("/users")
    public String createUser(AccountDto accountDto) {

        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDto, Account.class);

        // 패스워드 암호화
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // 저장
        userService.createUser(account);

        return "redirect:/";
    }



}

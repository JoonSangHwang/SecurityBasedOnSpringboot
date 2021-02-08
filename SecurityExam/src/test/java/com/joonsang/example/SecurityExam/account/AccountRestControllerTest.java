package com.joonsang.example.SecurityExam.account;

import com.joonsang.example.SecurityExam.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional      // 독립적인 테스트를 위해 Rollback 트랜잭션
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    @DisplayName("[익명 사용자] index 페이지 접근")
    public void index_anonymous() throws Exception {
        mockMvc.perform(
                    get("/")
                    .with(anonymous())
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[익명 사용자] index 페이지 접근2")
    public void index_anonymous2() throws Exception {
        mockMvc.perform(
                    get("/")
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("[USER 권한 회원] index 페이지 접근")
    public void index_user() throws Exception {
        // joonsang 이라는 유저가 이미 로그인 된 상태로, 인덱스 페이지에 접근한 상태 Mocking
        mockMvc.perform(
                    get("/")
                    .with(user("joonsang").roles("USER"))
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @WithMockUser(username = "joonsang", roles = "USER")
    @DisplayName("[USER 권한 회원] index 페이지 접근2")
    public void index_user2() throws Exception {
        // joonsang 이라는 유저가 이미 로그인 된 상태로, 인덱스 페이지에 접근한 상태 Mocking
        mockMvc.perform(
                    get("/")
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("[ADMIN 권한 회원] index 페이지 접근")
    public void index_admin() throws Exception {
        // joonsang 이라는 유저가 이미 로그인 된 상태로, 인덱스 페이지에 접근한 상태 Mocking
        mockMvc.perform(
                    get("/")
                    .with(user("joonsang").roles("ADMIN"))
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("[USER 권한 회원] admin 페이지 접근")
    public void admin_user() throws Exception {
        // joonsang 이라는 유저가 이미 로그인 된 상태로, 인덱스 페이지에 접근한 상태 Mocking
        mockMvc.perform(
                    get("/")
                    .with(user("joonsang").roles("USER"))
                )
                .andDo(print())
                .andExpect(status().isForbidden())      // 403 에러
        ;
    }

    @Test
    @DisplayName("[ADMIN 권한 회원] admin 페이지 접근")
    public void admin_admin() throws Exception {
        // joonsang 이라는 유저가 이미 로그인 된 상태로, 인덱스 페이지에 접근한 상태 Mocking
        mockMvc.perform(
                    get("/")
                    .with(user("joonsang").roles("ADMIN"))
        )
                .andDo(print())
                .andExpect(status().isOk())      // 403 에러
        ;
    }

    @Test
    @DisplayName("Form 로그인")
    public void form_login() throws Exception {
        Account account = new Account();
        account.setUsername("joonsang");
        account.setPassword("123");
        account.setRole("USER");
        accountService.createNewAccount(account);

        mockMvc.perform(
                    formLogin()
                    .user("joonsang").password("123")
        )
                .andDo(print())
                .andExpect(status().isOk())      // 403 에러
        ;
    }
}
package com.joonsang.example.Authentication_Ajax.service.impl;

import com.joonsang.example.Authentication_Ajax.domain.Account;
import com.joonsang.example.Authentication_Ajax.repo.UserRepository;
import com.joonsang.example.Authentication_Ajax.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void createUser(Account account) {
        userRepository.save(account);
    }
}

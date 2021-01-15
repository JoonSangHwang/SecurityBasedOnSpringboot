package com.joonsang.example.Authentication_Form.service.impl;

import com.joonsang.example.Authentication_Form.domain.Account;
import com.joonsang.example.Authentication_Form.repo.UserRepository;
import com.joonsang.example.Authentication_Form.service.UserService;
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

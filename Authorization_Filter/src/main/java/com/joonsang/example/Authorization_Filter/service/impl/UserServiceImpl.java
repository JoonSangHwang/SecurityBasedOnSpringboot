package com.joonsang.example.Authorization_Filter.service.impl;

import com.joonsang.example.Authorization_Filter.domain.entity.Account;
import com.joonsang.example.Authorization_Filter.repo.UserRepository;
import com.joonsang.example.Authorization_Filter.service.UserService;
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

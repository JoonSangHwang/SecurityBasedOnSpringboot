package com.joonsang.example.Authorization_Filter.repo;

import com.joonsang.example.Authorization_Filter.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}

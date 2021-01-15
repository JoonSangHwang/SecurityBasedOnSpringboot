package com.joonsang.example.Authentication_Form.repo;

import com.joonsang.example.Authentication_Form.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}

package com.joonsang.example.Authentication_Ajax.repo;

import com.joonsang.example.Authentication_Ajax.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
}

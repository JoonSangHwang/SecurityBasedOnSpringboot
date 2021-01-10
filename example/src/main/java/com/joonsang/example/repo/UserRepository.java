package com.joonsang.example.repo;

import com.joonsang.example.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
}

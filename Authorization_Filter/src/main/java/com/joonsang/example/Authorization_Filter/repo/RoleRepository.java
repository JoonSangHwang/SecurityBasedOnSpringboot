package com.joonsang.example.Authorization_Filter.repo;


import com.joonsang.example.Authorization_Filter.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);

    @Override
    void delete(Role role);

}

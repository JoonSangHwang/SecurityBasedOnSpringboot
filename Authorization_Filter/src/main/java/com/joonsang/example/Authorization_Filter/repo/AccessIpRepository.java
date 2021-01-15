package com.joonsang.example.Authorization_Filter.repo;


import com.joonsang.example.Authorization_Filter.domain.entity.AccessIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessIpRepository extends JpaRepository<AccessIp, Long> {

    AccessIp findByIpAddress(String IpAddress);
}

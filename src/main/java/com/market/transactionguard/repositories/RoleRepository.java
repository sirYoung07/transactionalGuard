package com.market.transactionguard.repositories;

import com.market.transactionguard.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByAuthority(String authority);

}

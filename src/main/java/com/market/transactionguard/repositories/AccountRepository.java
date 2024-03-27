package com.market.transactionguard.repositories;

import com.market.transactionguard.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

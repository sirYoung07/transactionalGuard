package com.market.transactionguard.repositories;

import com.market.transactionguard.entities.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDetailsRepository extends JpaRepository<AccountDetails, Long> {
}

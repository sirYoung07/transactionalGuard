package com.market.transactionguard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<com.market.transactionguard.entities.Transaction, Long> {
}

package com.market.transactionguard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Product extends JpaRepository<com.market.transactionguard.entities.Product, Long> {
}

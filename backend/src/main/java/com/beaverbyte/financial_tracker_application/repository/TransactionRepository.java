package com.beaverbyte.financial_tracker_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beaverbyte.financial_tracker_application.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{
}

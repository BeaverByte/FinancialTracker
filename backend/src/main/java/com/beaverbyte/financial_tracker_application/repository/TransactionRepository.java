package com.beaverbyte.financial_tracker_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beaverbyte.financial_tracker_application.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{

    // List<Transaction> findAll();

    // Transaction findById(int theId);

    // Transaction save(Transaction theTransaction);

    // void deleteById(int theId);
    
}

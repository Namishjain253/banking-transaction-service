package org.hsbc.banking.transaction.repository;

import org.hsbc.banking.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
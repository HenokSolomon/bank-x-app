package com.bankx.core.domain.repository;

import com.bankx.core.domain.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, UUID> {

}

package com.bankx.core.domain.repository;

import com.bankx.core.domain.entity.FinancialTransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialTransactionItemRepository extends JpaRepository<FinancialTransactionItem, UUID> {

}

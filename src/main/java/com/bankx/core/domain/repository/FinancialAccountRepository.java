package com.bankx.core.domain.repository;

import com.bankx.core.domain.entity.Customer;
import com.bankx.core.domain.entity.FinancialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, UUID> {

}

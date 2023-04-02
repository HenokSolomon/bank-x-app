package com.bankx.core.domain.repository;

import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findFirstByAccountNumber(final String accountNumber);
}

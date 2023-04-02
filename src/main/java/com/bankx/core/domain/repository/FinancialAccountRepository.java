package com.bankx.core.domain.repository;

import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, UUID> {

    List<FinancialAccount> findAllByAccountId(final UUID accountId);

    FinancialAccount findFirstByAccountIdAndFinancialAccountTypeEnum(final UUID accountId, final FinancialAccountTypeEnum financialAccountTypeEnum);

}

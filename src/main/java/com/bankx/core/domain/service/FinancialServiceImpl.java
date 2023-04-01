package com.bankx.core.domain.service;


import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.repository.FinancialAccountRepository;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service("financialService")
@AllArgsConstructor
public class FinancialServiceImpl implements FinancialService {

    private final FinancialAccountRepository financialAccountRepository;

    @Override
    public FinancialAccount createFinancialAccount(@NonNull FinancialAccountTypeEnum financialAccountType, @NonNull UUID accountId) {

        FinancialAccount financialAccount = FinancialAccount.builder()
                .financialAccountTypeEnum(financialAccountType)
                .accountId(accountId)
                .availableBalance(0.0)
                .build();

        financialAccount = financialAccountRepository.save(financialAccount);

        return financialAccount;
    }



}

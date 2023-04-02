package com.bankx.core.domain.service;

import com.bankx.core.domain.entity.FinancialTransaction;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.domain.types.FinancialTransactionTypeEnum;
import com.bankx.core.dto.TransactionDetailDto;
import lombok.NonNull;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.UUID;

public interface FinancialTransactionService {

    @Transactional
    FinancialTransaction registerFinancialTransaction(@NonNull FinancialTransactionTypeEnum financialTransactionType, @NonNull UUID accountId, double amount,
                                                      Map<String, Object> currentTxnParameters, Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccounts);

    TransactionDetailDto getTransactionDetail(final String referenceNumber);
}

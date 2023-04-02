package com.bankx.core.domain.journal;

import com.bankx.core.domain.entity.FinancialTransactionItem;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FinancialTransactionTemplate {

    List<FinancialTransactionItem> calculateAndGetTransactionItems(UUID financialTransactionId, double amount,
                                                                   Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccounts, Map<String, Object> currentTransactionParameter);
}

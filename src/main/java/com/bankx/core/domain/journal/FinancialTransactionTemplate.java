package com.bankx.core.domain.journal;

import com.bankx.core.domain.entity.FinancialTransactionItem;

public interface FinancialTransactionTemplate {

    FinancialTransactionItem calculateAndGetTransactionItems();
}

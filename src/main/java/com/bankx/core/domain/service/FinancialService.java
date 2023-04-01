package com.bankx.core.domain.service;

import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import lombok.NonNull;

import java.util.UUID;

public interface FinancialService {

    FinancialAccount createFinancialAccount(@NonNull FinancialAccountTypeEnum financialAccountType, @NonNull UUID accountId);
}

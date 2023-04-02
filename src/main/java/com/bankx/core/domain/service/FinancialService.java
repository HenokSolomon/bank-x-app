package com.bankx.core.domain.service;

import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.entity.FinancialTransaction;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.dto.AccountTransferDto;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface FinancialService {

    FinancialAccount createFinancialAccount(@NonNull FinancialAccountTypeEnum financialAccountType, @NonNull UUID accountId);

    FinancialTransaction payCustomerSignupBonus(@NonNull UUID customerAccountId, double bonusAmount,
                                                @NonNull UUID customerSavingAccountId);

    List<FinancialAccount> getFinancialAccounts(@NonNull UUID accountId);

    FinancialAccount getFinancialAccountByAccountIdAndFinancialAccountType(@NonNull UUID accountId, @NonNull FinancialAccountTypeEnum financialAccountType);

    AccountTransferDto transferFromSavingToCurrentAccount(@NonNull String accountNumber, double amount);

    FinancialTransaction transferFromSavingToCurrentAccount(@NonNull UUID accountId,
                                                            @NonNull UUID savingFinancialAccountId,
                                                            @NonNull UUID currentFinancialAccountId, double amount);

    AccountTransferDto transferFromCurrentToSavingAccount(@NonNull String accountNumber, double amount);

    FinancialTransaction transferFromCurrentToSavingAccount(@NonNull UUID accountId,
                                                            @NonNull UUID savingFinancialAccountId,
                                                            @NonNull UUID currentFinancialAccountId, double amount);

    AccountTransferDto bankChargeCustomerForPayment(@NonNull String bankInstitutionAccountNumber,
                                                    @NonNull String customerAccountNumber, double amount);

    AccountTransferDto bankAcceptPaymentToCustomerSaving(@NonNull String bankInstitutionAccountNumber,
                                                         @NonNull String customerAccountNumber, double amount);

}

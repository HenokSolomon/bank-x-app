package com.bankx.core.domain.service;


import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.entity.FinancialTransaction;
import com.bankx.core.domain.repository.FinancialAccountRepository;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.domain.types.FinancialTransactionTypeEnum;
import com.bankx.core.util.Constants;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service("financialService")
@AllArgsConstructor
public class FinancialServiceImpl implements FinancialService {


    private final AccountService accountService;
    private final FinancialAccountRepository financialAccountRepository;
    private final FinancialTransactionService financialTransactionService;
    private Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccountCache;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

        final Account systemAccount = accountService.findByAccountNumber(Constants.SYSTEM_ACCOUNT_ACCOUNT_NUMBER);
        if (null == systemAccount) {
            throw new RuntimeException("unable to locate system account");
        }

        List<FinancialAccount> systemOwnedFinancialAccounts =
                financialAccountRepository.findAllByAccountId(systemAccount.getAccountId());


        if (ListUtils.isEmpty(systemOwnedFinancialAccounts)) {
            throw new RuntimeException("unable to locate system owned financial accounts ");
        }

        systemOwnedFinancialAccountCache = new HashMap<>();

        systemOwnedFinancialAccounts
                .stream()
                .forEach(fa ->
                        systemOwnedFinancialAccountCache.put(fa.getFinancialAccountTypeEnum(),
                                fa.getFinancialAccountId()));

    }

    @Override
    public FinancialAccount createFinancialAccount(@NonNull FinancialAccountTypeEnum financialAccountType,
                                                   @NonNull UUID accountId) {

        FinancialAccount financialAccount = FinancialAccount.builder()
                .financialAccountTypeEnum(financialAccountType)
                .accountId(accountId)
                .availableBalance(0.0)
                .build();

        financialAccount = financialAccountRepository.save(financialAccount);

        return financialAccount;
    }


    @Override
    public List<FinancialAccount> getFinancialAccounts(@NonNull UUID accountId) {
        return financialAccountRepository.findAllByAccountId(accountId);
    }

    @Override
    public FinancialAccount getFinancialAccountByAccountIdAndFinancialAccountType(@NonNull UUID accountId,
                                                                                  @NonNull FinancialAccountTypeEnum financialAccountType) {
        return financialAccountRepository.findFirstByAccountIdAndFinancialAccountTypeEnum(accountId,
                financialAccountType);
    }

    @Override
    public FinancialTransaction payCustomerSignupBonus(@NonNull UUID customerAccountId, double bonusAmount,
                                                       @NonNull UUID customerSavingAccountId) {

        Map<String, Object> txnParameters = new HashMap<>();
        txnParameters.put(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID, customerSavingAccountId);

        FinancialTransaction transaction =
                financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.CUSTOMER_SIGNUP_BONUS_TRANSACTION,
                customerAccountId, bonusAmount, txnParameters, systemOwnedFinancialAccountCache);

        return transaction;
    }

    @Override
    public FinancialTransaction transferFromSavingToCurrentAccount(@NonNull UUID accountId,
                                                                   @NonNull UUID savingFinancialAccountId,
                                                                   @NonNull UUID currentFinancialAccountId, double amount) {

        Map<String, Object> txnParameter = new HashMap<>();
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID, savingFinancialAccountId);
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID, currentFinancialAccountId);

        FinancialTransaction transaction =
                financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.CUSTOMER_TRANSFER_FROM_SAVING_TO_CURRENT_ACCOUNT_TRANSACTION,
                accountId, amount, txnParameter, systemOwnedFinancialAccountCache);

        return transaction;
    }

    @Override
    public FinancialTransaction transferFromCurrentToSavingAccount(@NonNull UUID accountId,
                                                                   @NonNull UUID savingFinancialAccountId,
                                                                   @NonNull UUID currentFinancialAccountId, double amount) {

        Map<String, Object> txnParameter = new HashMap<>();
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID, savingFinancialAccountId);
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID, currentFinancialAccountId);

        FinancialTransaction transaction =
                financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.CUSTOMER_TRANSFER_FROM_CURRENT_TO_SAVING_ACCOUNT_TRANSACTION,
                        accountId, amount, txnParameter, systemOwnedFinancialAccountCache);

        return transaction;
    }

}

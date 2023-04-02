package com.bankx.core.domain.service;


import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.entity.FinancialTransaction;
import com.bankx.core.domain.entity.FinancialTransactionItem;
import com.bankx.core.domain.repository.FinancialAccountRepository;
import com.bankx.core.domain.repository.FinancialTransactionItemRepository;
import com.bankx.core.domain.repository.FinancialTransactionRepository;
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


    private Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccountCache;

    private final AccountService accountService;
    private final FinancialAccountRepository financialAccountRepository;
    private final FinancialTransactionService financialTransactionService;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

        final Account systemAccount = accountService.findByAccountNumber(Constants.SYSTEM_ACCOUNT_ACCOUNT_NUMBER);
        if (null == systemAccount) {
            throw new RuntimeException("unable to locate system account");
        }

        List<FinancialAccount> systemOwnedFinancialAccounts =
                financialAccountRepository.findAllByAccountId(systemAccount.getAccountId());


        if(ListUtils.isEmpty(systemOwnedFinancialAccounts)) {
            throw new RuntimeException("unable to locate system owned financial accounts ");
        }

        systemOwnedFinancialAccountCache = new HashMap<>();

        systemOwnedFinancialAccounts
                .stream()
                .forEach(fa ->
                        systemOwnedFinancialAccountCache.put(fa.getFinancialAccountTypeEnum(), fa.getFinancialAccountId()));

    }

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


    @Override
    public List<FinancialAccount> getFinancialAccounts(@NonNull UUID accountId) {
        return financialAccountRepository.findAllByAccountId(accountId);
    }


    @Override
    public FinancialTransaction payCustomerSignupBonus(@NonNull UUID customerAccountId, double bonusAmount, @NonNull UUID customerSavingAccountId) {

        Map<String, Object> currentTxnParameters = new HashMap<>();
        currentTxnParameters.put(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID, customerSavingAccountId);

        FinancialTransaction transaction = financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.CUSTOMER_SIGNUP_BONUS_TRANSACTION,
                customerAccountId, bonusAmount, currentTxnParameters, systemOwnedFinancialAccountCache);

        return transaction;
    }
}

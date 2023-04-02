package com.bankx.core.domain.service;


import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.entity.FinancialTransaction;
import com.bankx.core.domain.entity.FinancialTransactionItem;
import com.bankx.core.domain.exception.ServiceException;
import com.bankx.core.domain.journal.FinancialTransactionTemplate;
import com.bankx.core.domain.repository.AccountRepository;
import com.bankx.core.domain.repository.FinancialAccountRepository;
import com.bankx.core.domain.repository.FinancialTransactionItemRepository;
import com.bankx.core.domain.repository.FinancialTransactionRepository;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.domain.types.FinancialTransactionTypeEnum;
import com.bankx.core.dto.TransactionDetailDto;
import com.bankx.core.dto.TransactionDetailDto.JournalEntryItem;
import com.bankx.core.util.Constants;
import com.bankx.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service("financialTransactionService")
@AllArgsConstructor
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;
    private final FinancialTransactionItemRepository financialTransactionItemRepository;
    private final FinancialAccountRepository financialAccountRepository;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public FinancialTransaction registerFinancialTransaction(@NonNull FinancialTransactionTypeEnum financialTransactionType, @NonNull UUID accountId, double amount,
                                                             Map<String, Object> currentTxnParameters,
                                                             Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccounts) {

        FinancialTransaction financialTransaction = FinancialTransaction.builder()
                .financialTransactionTypeEnum(financialTransactionType)
                .accountId(accountId)
                .amount(amount)
                .referenceNumber(RandomUtil.generateReferenceNumber())
                .timestamp(LocalDateTime.now())
                .isSettled(false)
                .build();

        financialTransaction = financialTransactionRepository.save(financialTransaction);

        /*calculate and get transaction journal entries */
        FinancialTransactionTemplate template = financialTransactionType.getFinancialTransactionTemplate();

        List<FinancialTransactionItem> financialTransactionItems =
                template.calculateAndGetTransactionItems(financialTransaction.getFinancialTransactionId(),
                amount, systemOwnedFinancialAccounts, currentTxnParameters);

        for (FinancialTransactionItem item : financialTransactionItems) {

            /* record the entry */
            financialTransactionItemRepository.save(item);

            /* calculate account balance for financial account associated with current entry */
            FinancialAccount financialAccount = financialAccountRepository.getById(item.getFinancialAccountId());
            double availableBalance = financialAccount.getAvailableBalance();

            if (financialAccount.getFinancialAccountTypeEnum().getAccountCodeType().isNormalDebit()) {
                /* normal debit accounts increase when debited , decrease when credited */
                availableBalance = availableBalance + item.getDebitAmount() - item.getCreditAmount();

            } else {
                /* normal debit false accounts increase when credited , decrease when debited */
                availableBalance = availableBalance - item.getDebitAmount() + item.getCreditAmount();
            }

            /*finally update the financial_account  with the new balance */
            financialAccount.setAvailableBalance(availableBalance);
            financialAccountRepository.save(financialAccount);

        }

        return financialTransaction;
    }


    public TransactionDetailDto getTransactionDetail(final String referenceNumber) {

        Optional<FinancialTransaction> optionalFinancialTransaction =
                financialTransactionRepository.findFirstByReferenceNumber(referenceNumber);
        if (optionalFinancialTransaction.isEmpty()) {
            throw new ServiceException("transaction with reference number " + referenceNumber + " can't be found");
        }

        FinancialTransaction financialTransaction = optionalFinancialTransaction.get();

        List<FinancialTransactionItem> financialTransactionItems = financialTransactionItemRepository.
                findAllByFinancialTransactionIdOrderByLineNumberAsc(financialTransaction.getFinancialTransactionId());

        Account txnByAccount = accountRepository.getById(financialTransaction.getAccountId());

        List<JournalEntryItem> journalEntryItems = new ArrayList<>();

        financialTransactionItems.forEach(item -> {

            var finAccount = financialAccountRepository.findById(item.getFinancialAccountId());

            var entry = JournalEntryItem.builder()
                    .accountType(finAccount.map(fa -> fa.getFinancialAccountTypeEnum().name()).orElse(null))
                    .creditAmount(item.getCreditAmount())
                    .debitAmount(item.getDebitAmount())
                    .lineNumber(item.getLineNumber())
                    .description(item.getDescription())
                    .build();

            journalEntryItems.add(entry);

        });

        return TransactionDetailDto.builder()
                .transactionReference(financialTransaction.getReferenceNumber())
                .transactionType(financialTransaction.getFinancialTransactionTypeEnum().name())
                .accountNumber(txnByAccount.getAccountNumber())
                .amount(financialTransaction.getAmount())
                .timestamp(financialTransaction.getTimestamp().format(DateTimeFormatter.ISO_DATE))
                .journalEntryItems(journalEntryItems)
                .message("request successful")
                .statusCode(Constants.API_RESPONSE_CODE_SUCCESS)
                .build();

    }

}

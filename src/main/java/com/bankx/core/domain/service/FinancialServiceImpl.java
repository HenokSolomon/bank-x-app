package com.bankx.core.domain.service;


import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.entity.Customer;
import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.entity.FinancialTransaction;
import com.bankx.core.domain.exception.ServiceException;
import com.bankx.core.domain.repository.CustomerRepository;
import com.bankx.core.domain.repository.FinancialAccountRepository;
import com.bankx.core.domain.types.AccountTypeEnum;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.domain.types.FinancialTransactionTypeEnum;
import com.bankx.core.dto.AccountTransferDto;
import com.bankx.core.util.Constants;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.bankx.core.util.Constants.*;

@Slf4j
@Service("financialService")
@AllArgsConstructor
public class FinancialServiceImpl implements FinancialService {

    private final FinancialAccountRepository financialAccountRepository;
    private final CustomerRepository customerRepository;

    private final FinancialTransactionService financialTransactionService;
    private final MessagingService messagingService;
    private final AccountService accountService;

    private final Map<FinancialAccountTypeEnum, UUID> SYSTEM_OWNED_FINANCIAL_ACCOUNT_CACHE = new HashMap<>();

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

        systemOwnedFinancialAccounts
                .forEach(fa ->
                        SYSTEM_OWNED_FINANCIAL_ACCOUNT_CACHE.put(fa.getFinancialAccountTypeEnum(),
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

        var transaction = financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.CUSTOMER_SIGNUP_BONUS_TRANSACTION,
                customerAccountId, bonusAmount, txnParameters, SYSTEM_OWNED_FINANCIAL_ACCOUNT_CACHE);

        sendAccountActivityNotificationEmail(customerAccountId, FinancialAccountTypeEnum.SAVING_ACCOUNT, bonusAmount, transaction.getReferenceNumber(), true);

        return transaction;
    }

    @Override
    public AccountTransferDto customerTransferFromSavingToCurrentAccount(@NonNull String accountNumber, double amount) {

        if(amount <= 0d) {
            throw new ServiceException("transaction amount should be greater than zero");
        }

        final Account customerAccount = accountService.findByAccountNumber(accountNumber);
        FinancialAccount savingAccount = getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId()
                        , FinancialAccountTypeEnum.SAVING_ACCOUNT);

        FinancialAccount currentAccount = getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId()
                        , FinancialAccountTypeEnum.CURRENT_ACCOUNT);

        if (savingAccount.getAvailableBalance() < amount) {
            throw new ServiceException("insufficient saving account balance , current saving balance is " + savingAccount.getAvailableBalance());
        }

        /* record transfer transaction */
        FinancialTransaction transaction = transferFromSavingToCurrentAccount(customerAccount.getAccountId(),
                        savingAccount.getFinancialAccountId(), currentAccount.getFinancialAccountId(), amount);


        /* send notification for customer about account transfer */
        sendAccountActivityNotificationEmail(customerAccount.getAccountId(), savingAccount.getFinancialAccountTypeEnum(),
                amount, transaction.getReferenceNumber(), false);

        return AccountTransferDto.builder()
                .transactionReference(transaction.getReferenceNumber())
                .statusCode(API_RESPONSE_CODE_SUCCESS)
                .message("request successful, transaction detail can be checked using </transaction/getTransactionDetails> api on postman")
                .build();

    }


    @Override
    public FinancialTransaction transferFromSavingToCurrentAccount(@NonNull UUID accountId,
                                                                   @NonNull UUID savingFinancialAccountId,
                                                                   @NonNull UUID currentFinancialAccountId,
                                                                   double amount) {

        Map<String, Object> txnParameter = new HashMap<>();
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID, savingFinancialAccountId);
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID, currentFinancialAccountId);

        return financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.CUSTOMER_TRANSFER_FROM_SAVING_TO_CURRENT_ACCOUNT_TRANSACTION,
                accountId, amount, txnParameter, SYSTEM_OWNED_FINANCIAL_ACCOUNT_CACHE);
    }


    @Override
    public AccountTransferDto customerTransferFromCurrentToSavingAccount(@NonNull String accountNumber, double amount) {

        if(amount <= 0d) {
            throw new ServiceException("transaction amount should be greater than zero");
        }

        final Account customerAccount = accountService.findByAccountNumber(accountNumber);
        FinancialAccount savingAccount = getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId()
                        , FinancialAccountTypeEnum.SAVING_ACCOUNT);

        FinancialAccount currentAccount = getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId()
                        , FinancialAccountTypeEnum.CURRENT_ACCOUNT);

        if (currentAccount.getAvailableBalance() < amount) {
            throw new ServiceException("insufficient saving account balance , current saving balance is " + savingAccount.getAvailableBalance());
        }

        /* record transfer transaction */
        FinancialTransaction transaction = transferFromCurrentToSavingAccount(customerAccount.getAccountId(),
                savingAccount.getFinancialAccountId(), currentAccount.getFinancialAccountId(), amount);

        /* send notification for customer about account transfer */
        sendAccountActivityNotificationEmail(customerAccount.getAccountId(), currentAccount.getFinancialAccountTypeEnum(),
                amount, transaction.getReferenceNumber(), false);

        return AccountTransferDto.builder()
                .transactionReference(transaction.getReferenceNumber())
                .statusCode(API_RESPONSE_CODE_SUCCESS)
                .message("request successful, transaction detail can be checked using </transaction/getTransactionDetails> api on postman")
                .build();

    }

    @Override
    public FinancialTransaction transferFromCurrentToSavingAccount(@NonNull UUID accountId,
                                                                   @NonNull UUID savingFinancialAccountId,
                                                                   @NonNull UUID currentFinancialAccountId,
                                                                   double amount) {

        Map<String, Object> txnParameter = new HashMap<>();
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID, savingFinancialAccountId);
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID, currentFinancialAccountId);

        return financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.CUSTOMER_TRANSFER_FROM_CURRENT_TO_SAVING_ACCOUNT_TRANSACTION,
                accountId, amount, txnParameter, SYSTEM_OWNED_FINANCIAL_ACCOUNT_CACHE);
    }


    @Override
    public AccountTransferDto bankChargeCustomerForPayment(@NonNull String bankInstitutionAccountNumber,
                                                           @NonNull String customerAccountNumber, double amount) {

        if (amount <= 0d) {
            throw new ServiceException("transaction amount should be greater than zero");
        }

        final Account customerAccount = accountService.findByAccountNumber(customerAccountNumber);

        final Account bankInstAccount = accountService.findByAccountNumber(bankInstitutionAccountNumber);
        if (!bankInstAccount.getAccountTypeEnum().equals(AccountTypeEnum.BUSINESS)) {
            throw new ServiceException("account with account number " + bankInstitutionAccountNumber + " is not a " +
                    "valid bank institution " +
                    "account ");
        }

        final FinancialAccount customerCurrentFinAcc =
                getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId(),
                        FinancialAccountTypeEnum.CURRENT_ACCOUNT);
        if (customerCurrentFinAcc.getAvailableBalance() < amount) {
            throw new ServiceException("insufficient account balance , current  balance is " + customerCurrentFinAcc.getAvailableBalance());
        }

        /*get banks payable tracking fin account */
        final FinancialAccount bankPayableFinAcc =
                getFinancialAccountByAccountIdAndFinancialAccountType(bankInstAccount.getAccountId(),
                        FinancialAccountTypeEnum.THIRD_PARTY_BANK_PAYABLE);

        Map<String, Object> txnParameter = new HashMap<>();

        txnParameter.put(Constants.TXN_PARAMETER_THIRD_PARTY_BANK_PAYABLE_ACCOUNT_ID,
                bankPayableFinAcc.getFinancialAccountId());
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID,
                customerCurrentFinAcc.getFinancialAccountId());
        txnParameter.put(Constants.TXN_PARAMETER_CURRENT_ACCOUNT_PAYMENT_FEE_RATE,
                Constants.CURRENT_ACCOUNT_PAYMENT_PERCENTAGE_FEE_RATE_PERCENTAGE);

        FinancialTransaction transaction =
                financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.BANK_CHARGE_CURRENT_ACCOUNT_FOR_PAYMENT_TRANSACTION,
                bankInstAccount.getAccountId(), amount, txnParameter, SYSTEM_OWNED_FINANCIAL_ACCOUNT_CACHE);

        sendAccountActivityNotificationEmail(customerAccount.getAccountId(), customerCurrentFinAcc.getFinancialAccountTypeEnum(),
                amount, transaction.getReferenceNumber(), true);

        return AccountTransferDto.builder()
                .transactionReference(transaction.getReferenceNumber())
                .statusCode(Constants.API_RESPONSE_CODE_SUCCESS)
                .message("request successful, transaction detail can be checked using </transaction/getTransactionDetails> api on postman")
                .build();

    }


    @Override
    public AccountTransferDto bankAcceptPaymentToCustomerSaving(@NonNull String bankInstitutionAccountNumber,
                                                           @NonNull String customerAccountNumber, double amount) {

        if (amount <= 0d) {
            throw new ServiceException("transaction amount should be greater than zero");
        }

        final Account customerAccount = accountService.findByAccountNumber(customerAccountNumber);

        final Account bankInstAccount = accountService.findByAccountNumber(bankInstitutionAccountNumber);
        if (!bankInstAccount.getAccountTypeEnum().equals(AccountTypeEnum.BUSINESS)) {
            throw new ServiceException("account with account number " + bankInstitutionAccountNumber + " is not a " +
                    "valid bank institution " +
                    "account ");
        }

        final FinancialAccount customerSavingFinAcc =
                getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId(),
                        FinancialAccountTypeEnum.SAVING_ACCOUNT);

        if (customerSavingFinAcc.getAvailableBalance() < amount) {
            throw new ServiceException("insufficient account balance , current saving balance is " + customerSavingFinAcc.getAvailableBalance());
        }

        /*get banks payable tracking fin account */
        final FinancialAccount bankReceivableFinAcc =
                getFinancialAccountByAccountIdAndFinancialAccountType(bankInstAccount.getAccountId(),
                        FinancialAccountTypeEnum.THIRD_PARTY_BANK_RECEIVABLE);

        Map<String, Object> txnParameter = new HashMap<>();

        txnParameter.put(Constants.TXN_PARAMETER_THIRD_PARTY_BANK_RECEIVABLE_ACCOUNT_ID,
                bankReceivableFinAcc.getFinancialAccountId());
        txnParameter.put(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID,
                customerSavingFinAcc.getFinancialAccountId());
        txnParameter.put(Constants.TXN_PARAMETER_PAYMENT_TO_SAVING_INTEREST_RATE,
                Constants.SAVING_ACCOUNT_CREDIT_PAYMENT_INTEREST_RATE_PERCENTAGE);
        txnParameter.put(Constants.TXN_PARAMETER_SAVING_ACCOUNT_BALANCE, customerSavingFinAcc.getAvailableBalance());

        FinancialTransaction transaction =
                financialTransactionService.registerFinancialTransaction(FinancialTransactionTypeEnum.BANK_ACCEPT_PAYMENT_TO_SAVING_ACCOUNT,
                        bankInstAccount.getAccountId(), amount, txnParameter, SYSTEM_OWNED_FINANCIAL_ACCOUNT_CACHE);

        sendAccountActivityNotificationEmail(customerAccount.getAccountId(), customerSavingFinAcc.getFinancialAccountTypeEnum(),
                amount, transaction.getReferenceNumber(), false);

        return AccountTransferDto.builder()
                .transactionReference(transaction.getReferenceNumber())
                .statusCode(Constants.API_RESPONSE_CODE_SUCCESS)
                .message("request successful, transaction detail can be checked using </transaction/getTransactionDetails> api on postman")
                .build();

    }


    private void sendAccountActivityNotificationEmail(UUID toCustomerAccountId, FinancialAccountTypeEnum financialAccountTypeEnum,
                                                      double amount, String txnReference, boolean isDebit) {

        try {

            Customer customer = customerRepository.findFirstByAccountId(toCustomerAccountId);

            Map<String, Object> templateParam = new HashMap<>();
            templateParam.put("recipientName", customer.getFirstName() + " " + customer.getLastName());
            templateParam.put("accountType", financialAccountTypeEnum.name());
            templateParam.put("actionType", isDebit ? "Debited" : "Credited");
            templateParam.put("amount", amount);
            templateParam.put("transactionReference", txnReference);
            templateParam.put("transactionDate", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));

            messagingService.sendEmail(DEFAULT_EMAIL_FROM, customer.getEmail(), DEFAULT_ACCOUNT_ACTIVITY_EMAIL_SUBJECT,
                    DEFAULT_ACCOUNT_ACTIVITY_EMAIL_TEMPLATE_NAME, templateParam);

        } catch (Exception e) {
            /*don't fail the transaction just because messaging failed , just catch and log the error */
            log.error("CustomerServiceImpl : error sending notification email ", e);
        }
    }


}

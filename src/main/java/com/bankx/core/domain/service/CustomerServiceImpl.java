package com.bankx.core.domain.service;

import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.entity.Customer;
import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.entity.FinancialTransaction;
import com.bankx.core.domain.exception.ServiceException;
import com.bankx.core.domain.repository.CustomerRepository;
import com.bankx.core.domain.types.AccountTypeEnum;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.dto.CustomerAccountBalanceDto;
import com.bankx.core.dto.AccountTransferDto;
import com.bankx.core.dto.CustomerDetailDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.bankx.core.util.Constants.*;

@Slf4j
@Service("customerService")
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final FinancialService financialService;
    private final MessagingService messagingService;


    @Transactional
    @Override
    public CustomerDetailDto createCustomerAccount(@NonNull final String firstName, @NonNull final String lastname,
                                                   @NonNull final String email) {

        if (StringUtils.isBlank(firstName)) {
            throw new ServiceException("customer first name is required");
        }

        validateEmail(email);

        /*create customer details*/
        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastname)
                .email(email)
                .build();

        customer = customerRepository.save(customer);

        /* create system account */
        final Account account = accountService.createAccount(AccountTypeEnum.CUSTOMER, null, null);

        customer.setAccountId(account.getAccountId());
        customerRepository.save(customer);

        /*create CURRENT & SAVING financial account */
        financialService.createFinancialAccount(FinancialAccountTypeEnum.CURRENT_ACCOUNT, account.getAccountId());
        final FinancialAccount savingAccount =
                financialService.createFinancialAccount(FinancialAccountTypeEnum.SAVING_ACCOUNT,
                        account.getAccountId());

        /* give bonus to their saving account */
        final FinancialTransaction bonusTxn =
                financialService.payCustomerSignupBonus(account.getAccountId(), SIGNUP_BONUS_AMOUNT,
                        savingAccount.getFinancialAccountId());

        /* send notification for customer about bonus credit */
        try {

            Map<String, Object> templateParam = new HashMap<>();

            messagingService.sendEmail(DEFAULT_EMAIL_FROM, email, DEFAULT_ACCOUNT_ACTIVITY_EMAIL_SUBJECT,
                    DEFAULT_ACCOUNT_ACTIVITY_EMAIL_TEMPLATE_NAME, templateParam);

        } catch (Exception e) {
            /*don't fail the transaction just because messaging failed , just catch and log the error */
            log.error("CustomerServiceImpl : error sending notification email ", e);
        }

        return CustomerDetailDto.builder()
                .firstName(firstName)
                .lastName(lastname)
                .email(email)
                .accountNumber(account.getAccountNumber())
                .currentAccountBalance(0d)
                .savingAccountBalance(bonusTxn.getAmount())
                .build();
    }


    @Override
    public CustomerAccountBalanceDto getAccountBalance(@NonNull String accountNumber) {

        final Account customerAccount = getAccount(accountNumber);

        List<FinancialAccount> financialAccounts =
                financialService.getFinancialAccounts(customerAccount.getAccountId());
        if (ListUtils.isEmpty(financialAccounts)) {
            throw new ServiceException("customer doesnt possess any financial account");
        }

        FinancialAccount savingAccount =
                financialAccounts.stream().filter(f -> f.getFinancialAccountTypeEnum().equals(FinancialAccountTypeEnum.SAVING_ACCOUNT)).findFirst().orElse(null);

        FinancialAccount currentAccount =
                financialAccounts.stream().filter(f -> f.getFinancialAccountTypeEnum().equals(FinancialAccountTypeEnum.CURRENT_ACCOUNT)).findFirst().orElse(null);

        return CustomerAccountBalanceDto.builder()
                .currentAccountBalance(currentAccount != null ? currentAccount.getAvailableBalance() : 0d)
                .savingAccountBalance(savingAccount != null ? savingAccount.getAvailableBalance() : 0d)
                .statusCode(API_RESPONSE_CODE_SUCCESS)
                .message("request successful")
                .build();
    }


    @Override
    public AccountTransferDto transferFromSavingToCurrentAccount(@NonNull String accountNumber,
                                                                 @NonNull double amount) {

        final Account customerAccount = getAccount(accountNumber);

        FinancialAccount savingAccount =
                financialService.getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId()
                        , FinancialAccountTypeEnum.SAVING_ACCOUNT);
        if (null == savingAccount) {
            throw new ServiceException("account holder doesn't possess saving account");
        }

        FinancialAccount currentAccount =
                financialService.getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId()
                        , FinancialAccountTypeEnum.CURRENT_ACCOUNT);
        if (null == currentAccount) {
            throw new ServiceException("account holder doesn't possess current account");
        }

        if (savingAccount.getAvailableBalance() < amount) {
            throw new ServiceException("insufficient saving account balance , current saving balance is " + savingAccount.getAvailableBalance());
        }

        /* record transfer transaction */
        FinancialTransaction transaction =
                financialService.transferFromSavingToCurrentAccount(customerAccount.getAccountId(),
                savingAccount.getFinancialAccountId(), currentAccount.getFinancialAccountId(), amount);


        /* send notification for customer about account transfer */
        try {

            final Customer customer = customerRepository.findFirstByAccountId(currentAccount.getAccountId());

            Map<String, Object> templateParam = new HashMap<>();

            messagingService.sendEmail(DEFAULT_EMAIL_FROM, customer.getEmail(), DEFAULT_ACCOUNT_ACTIVITY_EMAIL_SUBJECT,
                    DEFAULT_ACCOUNT_ACTIVITY_EMAIL_TEMPLATE_NAME, templateParam);

        } catch (Exception e) {
            /*don't fail the transaction just because messaging failed , just catch and log the error */
            log.error("CustomerServiceImpl : error sending notification email ", e);
        }


        return AccountTransferDto.builder()
                .transactionReference(transaction.getReferenceNumber())
                .statusCode(API_RESPONSE_CODE_SUCCESS)
                .message("request successful")
                .build();

    }


    @Override
    public AccountTransferDto transferFromCurrentToSavingAccount(@NonNull String accountNumber,
                                                                 @NonNull double amount) {

        final Account customerAccount = getAccount(accountNumber);

        FinancialAccount savingAccount =
                financialService.getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId()
                        , FinancialAccountTypeEnum.SAVING_ACCOUNT);
        if (null == savingAccount) {
            throw new ServiceException("account holder doesn't possess saving account");
        }

        FinancialAccount currentAccount =
                financialService.getFinancialAccountByAccountIdAndFinancialAccountType(customerAccount.getAccountId()
                        , FinancialAccountTypeEnum.CURRENT_ACCOUNT);
        if (null == currentAccount) {
            throw new ServiceException("account holder doesn't possess current account");
        }

        if (currentAccount.getAvailableBalance() < amount) {
            throw new ServiceException("insufficient saving account balance , current saving balance is " + savingAccount.getAvailableBalance());
        }

        /* record transfer transaction */
        FinancialTransaction transaction =
                financialService.transferFromCurrentToSavingAccount(customerAccount.getAccountId(),
                savingAccount.getFinancialAccountId(), currentAccount.getFinancialAccountId(), amount);


        /* send notification for customer about account transfer */
        try {

            final Customer customer = customerRepository.findFirstByAccountId(currentAccount.getAccountId());

            Map<String, Object> templateParam = new HashMap<>();

            messagingService.sendEmail(DEFAULT_EMAIL_FROM, customer.getEmail(), DEFAULT_ACCOUNT_ACTIVITY_EMAIL_SUBJECT,
                    DEFAULT_ACCOUNT_ACTIVITY_EMAIL_TEMPLATE_NAME, templateParam);

        } catch (Exception e) {
            /*don't fail the transaction just because messaging failed , just catch and log the error */
            log.error("CustomerServiceImpl : error sending notification email ", e);
        }


        return AccountTransferDto.builder()
                .transactionReference(transaction.getReferenceNumber())
                .statusCode(API_RESPONSE_CODE_SUCCESS)
                .message("request successful")
                .build();

    }


    private Account getAccount(String accountNumber) {

        if (StringUtils.isBlank(accountNumber)) {
            throw new ServiceException("invalid account number");
        }

        final Account customerAccount = accountService.findByAccountNumber(accountNumber);

        if (customerAccount == null) {
            throw new ServiceException("customer with account number " + accountNumber + " doesn't exist.");
        }

        return customerAccount;
    }

    private void validateEmail(String email) {

        if (StringUtils.isBlank(email)) {
            throw new ServiceException("customer email address is required");
        }

        final boolean match =
                (Pattern.compile(REGEX_MATCHER_VALID_EMAIL, Pattern.CASE_INSENSITIVE).matcher(email).matches());

        if (!match) {
            throw new ServiceException("the email address is not valid" + email);
        }

        var existingCustomerByEmail = customerRepository.findFirstByEmail(email);
        if (existingCustomerByEmail.isPresent()) {
            throw new ServiceException("email address is already used by another customer");
        }

    }


}

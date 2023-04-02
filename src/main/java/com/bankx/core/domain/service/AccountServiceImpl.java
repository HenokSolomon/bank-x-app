package com.bankx.core.domain.service;


import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.exception.ServiceException;
import com.bankx.core.domain.repository.AccountRepository;
import com.bankx.core.domain.types.AccountTypeEnum;
import com.bankx.core.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service("accountService")
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final int DEFAULT_ACCOUNT_NUMBER_LENGTH = 6;


    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(@NonNull AccountTypeEnum accountTypeEnum, String principal, String credentials) {

        Account account = Account.builder()
                .accountTypeEnum(accountTypeEnum)
                .principal(principal)
                .credential(credentials)
                .accountNumber(generateAccountNumber())
                .build();

        account = accountRepository.save(account);

        return account;
    }

    @Override
    public String generateAccountNumber() {
        return RandomUtil.generateNumber(DEFAULT_ACCOUNT_NUMBER_LENGTH);
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {

        if (StringUtils.isBlank(accountNumber)) {
            throw new ServiceException("invalid account number");
        }

        final Account account = accountRepository.findFirstByAccountNumber(accountNumber);
        if (account == null) {
            throw new ServiceException("account with accountNumber " + accountNumber + " doesn't exist.");
        }

        return account;
    }

    @Override
    public Account findAccountById(UUID accountId) {
        return accountRepository.getById(accountId);
    }

}

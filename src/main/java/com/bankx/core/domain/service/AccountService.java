package com.bankx.core.domain.service;

import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.types.AccountTypeEnum;

import java.util.UUID;

public interface AccountService {

    Account createAccount(AccountTypeEnum accountTypeEnum, String principal, String credentials);

    String generateAccountNumber();

    Account findByAccountNumber(String accountNumber);

    Account findAccountById(UUID accountId);
}

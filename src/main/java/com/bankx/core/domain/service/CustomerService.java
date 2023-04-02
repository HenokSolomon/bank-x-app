package com.bankx.core.domain.service;

import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.entity.Customer;
import com.bankx.core.dto.CustomerAccountBalanceDto;
import com.bankx.core.dto.AccountTransferDto;
import com.bankx.core.dto.CustomerDetailDto;
import lombok.NonNull;

import javax.transaction.Transactional;
import java.util.UUID;

public interface CustomerService {

    @Transactional
    CustomerDetailDto createCustomerAccount(@NonNull final String firstName, @NonNull final String lastname, @NonNull final String email);

    CustomerAccountBalanceDto getAccountBalance(@NonNull String accountNumber);

    Account getCustomerAccount(@NonNull String accountNumber);

    Customer getCustomerByAccountId(@NonNull UUID accountId);
}

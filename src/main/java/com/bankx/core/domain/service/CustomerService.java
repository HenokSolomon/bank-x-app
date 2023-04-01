package com.bankx.core.domain.service;

import com.bankx.core.dto.CustomerDetailDto;
import lombok.NonNull;

import javax.transaction.Transactional;

public interface CustomerService {

    @Transactional
    CustomerDetailDto createCustomerAccount(@NonNull final String firstName, @NonNull final String lastname, @NonNull final String email);
}

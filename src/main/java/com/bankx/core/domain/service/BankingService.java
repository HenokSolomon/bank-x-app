package com.bankx.core.domain.service;

import com.bankx.core.dto.AccountTransferDto;
import com.bankx.core.dto.AllBankInstitutionDto;
import lombok.NonNull;

public interface BankingService {

    AllBankInstitutionDto findAllBankInstitutions();

    AccountTransferDto bankChargeCustomerForPayment(@NonNull String bankInstitutionAccountNumber,
                                                    @NonNull String customerAccountNumber, double amount);

    AccountTransferDto bankAcceptPaymentToCustomerSaving(@NonNull String bankInstitutionAccountNumber,
                                                         @NonNull String customerAccountNumber, double amount);

}

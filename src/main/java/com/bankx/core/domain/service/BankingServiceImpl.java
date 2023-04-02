package com.bankx.core.domain.service;


import com.bankx.core.domain.entity.BankInstitution;
import com.bankx.core.domain.repository.BankInstitutionRepository;
import com.bankx.core.dto.AccountTransferDto;
import com.bankx.core.dto.AllBankInstitutionDto;
import com.bankx.core.dto.BankInstitutionDetailDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("bankingService")
@AllArgsConstructor
public class BankingServiceImpl implements BankingService {

    private final BankInstitutionRepository bankInstitutionRepository;
    private final AccountService accountService;
    private final FinancialService financialService;

    @Override
    public AllBankInstitutionDto findAllBankInstitutions() {

        AllBankInstitutionDto allBankInstitutions = new AllBankInstitutionDto();

        final List<BankInstitution> bankInstitutions = bankInstitutionRepository.findAll();

        bankInstitutions.stream().forEach(
                bankInstitution -> {
                    var bankAccount = accountService.findAccountById(bankInstitution.getAccountId());
                    allBankInstitutions
                            .getBankInstitutions()
                            .add(new BankInstitutionDetailDto(bankInstitution.getBankName(), bankAccount.getAccountNumber()));
                }
        );

        return allBankInstitutions;
    }

    @Override
    public AccountTransferDto bankChargeCustomerForPayment(@NonNull String bankInstitutionAccountNumber,
                                                           @NonNull String customerAccountNumber, double amount) {
        return financialService.bankChargeCustomerForPayment(bankInstitutionAccountNumber, customerAccountNumber, amount);
    }

    @Override
    public AccountTransferDto bankAcceptPaymentToCustomerSaving(@NonNull String bankInstitutionAccountNumber,
                                                                @NonNull String customerAccountNumber, double amount) {
        return financialService.bankAcceptPaymentToCustomerSaving(bankInstitutionAccountNumber, customerAccountNumber, amount);
    }

}

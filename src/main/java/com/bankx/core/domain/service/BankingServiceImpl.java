package com.bankx.core.domain.service;


import com.bankx.core.domain.entity.BankInstitution;
import com.bankx.core.domain.exception.ServiceException;
import com.bankx.core.domain.repository.BankInstitutionRepository;
import com.bankx.core.domain.types.AccountTypeEnum;
import com.bankx.core.dto.*;
import com.bankx.core.util.Constants;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
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

    @Override
    public BulkBankTransactionResponseDto processBulkBankTransactions(BulkBankTransactionRequestDto request) {

        if(StringUtils.isBlank(request.getBankInstitutionAccountNumber())) {
            throw new ServiceException("invalid bankInstitutionAccountNumber " + request.getBankInstitutionAccountNumber());
        }

        if(ListUtils.isEmpty(request.getTransactionRequests())) {
            throw new ServiceException("transaction item list is empty ");
        }

        var account = accountService.findByAccountNumber(request.getBankInstitutionAccountNumber());
        if(!account.getAccountTypeEnum().equals(AccountTypeEnum.BUSINESS)) {
            throw new ServiceException("bankInstitutionAccountNumber is not valid");
        }

        List<BulkBankTransactionItemResponseDto> bulkBankTransactionItemResponse = new ArrayList<>();

        request.getTransactionRequests().forEach(requestItem -> {

            if(requestItem.getBankTransactionType().equals(BankTransactionTypeEnum.CHARGE_CUSTOMER_ACCOUNT)) {

                try {

                   var txn = financialService.bankChargeCustomerForPayment(request.getBankInstitutionAccountNumber(), requestItem.getCustomerAccountNumber(), requestItem.getAmount());

                   bulkBankTransactionItemResponse.add(new BulkBankTransactionItemResponseDto(requestItem.getRequestReference(),
                           true, "success", txn.getTransactionReference()));

                } catch (Exception e) {

                    bulkBankTransactionItemResponse.add(new BulkBankTransactionItemResponseDto(requestItem.getRequestReference(),
                            false, e.getMessage(), null));
                }

            } else if (requestItem.getBankTransactionType().equals(BankTransactionTypeEnum.CREDIT_CUSTOMER_SAVING_ACCOUNT)) {

                try {

                    var txn = financialService.bankAcceptPaymentToCustomerSaving(request.getBankInstitutionAccountNumber(), requestItem.getCustomerAccountNumber(), requestItem.getAmount());

                    bulkBankTransactionItemResponse.add(new BulkBankTransactionItemResponseDto(requestItem.getRequestReference(),
                            true, "success", txn.getTransactionReference()));

                } catch (Exception e) {

                    bulkBankTransactionItemResponse.add(new BulkBankTransactionItemResponseDto(requestItem.getRequestReference(),
                            false, e.getMessage(), null));
                }
            } else {

                bulkBankTransactionItemResponse.add(new BulkBankTransactionItemResponseDto(requestItem.getRequestReference(),
                        false, "invalid transaction type", null));
            }

        });

        return BulkBankTransactionResponseDto.builder()
                .transactionItemResponse(bulkBankTransactionItemResponse)
                .message("success")
                .statusCode(Constants.API_RESPONSE_CODE_SUCCESS)
                .build();
    }

}

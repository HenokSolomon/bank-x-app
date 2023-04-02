package com.bankx.core.api;

import com.bankx.core.api.model.BankAccountPaymentRequest;
import com.bankx.core.domain.service.BankingService;
import com.bankx.core.domain.service.FinancialService;
import com.bankx.core.dto.AccountTransferDto;
import com.bankx.core.dto.AllBankInstitutionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = {"/bank"})
@RestController
@Slf4j
public class BankServicesRestEndpoint {

    private final BankingService bankingService;

    public BankServicesRestEndpoint(BankingService bankingService) {
        this.bankingService = bankingService;
    }


    @GetMapping("/getAllBankInstitutions")
    @ResponseBody
    public AllBankInstitutionDto getAllBankInstitutions() {
        return bankingService.findAllBankInstitutions();
    }


    @PostMapping("/bankChargeCustomerForPayment")
    @ResponseBody
    public AccountTransferDto bankChargeCustomerForPayment(@RequestBody BankAccountPaymentRequest request) {

        return bankingService.bankChargeCustomerForPayment(request.getBankInstitutionAccountNumber(),
                request.getCustomerAccountNumber(), request.getAmount());
    }

    @PostMapping("/bankAcceptPaymentToCustomerSaving")
    @ResponseBody
    public AccountTransferDto bankAcceptPaymentToCustomerSaving(@RequestBody BankAccountPaymentRequest request) {

        return bankingService.bankAcceptPaymentToCustomerSaving(request.getBankInstitutionAccountNumber(),
                request.getCustomerAccountNumber(), request.getAmount());
    }



}

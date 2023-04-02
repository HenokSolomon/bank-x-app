package com.bankx.core.api;

import com.bankx.core.domain.service.FinancialTransactionService;
import com.bankx.core.dto.TransactionDetailDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = {"/transaction"})
@RestController
@Slf4j
public class TransactionServicesRestEndpoint {

    private final FinancialTransactionService financialTransactionService;

    public TransactionServicesRestEndpoint(FinancialTransactionService financialTransactionService) {
        this.financialTransactionService = financialTransactionService;
    }

    @GetMapping("/getTransactionDetail")
    @ResponseBody
    public TransactionDetailDto getTransactionDetail(@RequestParam("referenceNumber") @NonNull String referenceNumber) {

        return financialTransactionService.getTransactionDetail(referenceNumber);
    }


}

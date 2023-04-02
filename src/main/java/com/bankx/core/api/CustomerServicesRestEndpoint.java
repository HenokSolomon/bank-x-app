package com.bankx.core.api;

import com.bankx.core.api.model.AccountTransferRequest;
import com.bankx.core.api.model.CreateCustomerRequest;
import com.bankx.core.domain.service.CustomerService;
import com.bankx.core.dto.AccountTransferDto;
import com.bankx.core.dto.CustomerAccountBalanceDto;
import com.bankx.core.dto.CustomerDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = {"/customer"})
@RestController
@Slf4j
public class CustomerServicesRestEndpoint {

    private final CustomerService customerService;

    public CustomerServicesRestEndpoint(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    @ResponseBody
    public CustomerDetailDto create(@RequestBody CreateCustomerRequest request) {

        var customerDetails = customerService.createCustomerAccount(request.getFirstName(),
                request.getLastName(), request.getEmail());

        return customerDetails;

    }


    @GetMapping("/getAccountBalance")
    @ResponseBody
    public CustomerAccountBalanceDto getAccountBalance(@RequestParam("accountNumber") String accountNumber) {

        return customerService.getAccountBalance(accountNumber);
    }

    @PostMapping("/transfer-to-current")
    @ResponseBody
    public AccountTransferDto transferToCurrent(@RequestBody AccountTransferRequest request) {

        var response = customerService.transferFromSavingToCurrentAccount(request.getAccountNumber(),
                request.getAmount());

        return response;
    }

    @PostMapping("/transfer-to-saving")
    @ResponseBody
    public AccountTransferDto transferToSaving(@RequestBody AccountTransferRequest request) {

        var response = customerService.transferFromCurrentToSavingAccount(request.getAccountNumber(),
                request.getAmount());

        return response;
    }

}

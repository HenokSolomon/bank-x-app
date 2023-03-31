package com.bankx.core.api;

import com.bankx.core.domain.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = {"/bank-services"})
@RestController
@Slf4j
public class BankServicesRestEndpoint {

    private final CustomerService customerService;

    public BankServicesRestEndpoint(CustomerService customerService) {
        this.customerService = customerService;
    }


}

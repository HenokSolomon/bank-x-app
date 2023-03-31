package com.bankx.core.api;

import com.bankx.core.api.model.CreateCustomerRequest;
import com.bankx.core.domain.service.CustomerService;
import com.bankx.core.dto.CustomerDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = {"/customer-services"})
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

        //fixme
        return null;
    }

    @GetMapping("/findAll")
    @ResponseBody
    public List<CustomerDetailDto> findAll() {
        //fixme
        return null;
    }

}

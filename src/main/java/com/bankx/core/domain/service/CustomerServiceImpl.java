package com.bankx.core.domain.service;

import com.bankx.core.domain.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    /* email address validation regex */
    public static final String REGEX_MATCHER_VALID_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String DEFAULT_INVITATION_EMAIL_SUBJECT = "bank account activity notification";
    private static final String DEFAULT_EMAIL_FROM = "solomonmail88@gmail.com";
    private static final String INVITATION_EMAIL_TEMPLATE_NAME = "account-notification-email-template";


    private final CustomerRepository customerRepository;
    private final MessagingService messagingService;


    public CustomerServiceImpl(CustomerRepository customerRepository, MessagingService messagingService) {
        this.customerRepository = customerRepository;
        this.messagingService = messagingService;
    }


}

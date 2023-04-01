package com.bankx.core.domain.service;

import com.bankx.core.domain.entity.Account;
import com.bankx.core.domain.entity.Customer;
import com.bankx.core.domain.entity.FinancialAccount;
import com.bankx.core.domain.exceptions.ServiceException;
import com.bankx.core.domain.repository.CustomerRepository;
import com.bankx.core.domain.types.AccountTypeEnum;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.dto.CustomerDetailDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Pattern;

@Slf4j
@Service("customerService")
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    /* email address validation regex */
    public static final String REGEX_MATCHER_VALID_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String DEFAULT_INVITATION_EMAIL_SUBJECT = "bank account activity notification";
    private static final String DEFAULT_EMAIL_FROM = "solomonmail88@gmail.com";
    private static final String INVITATION_EMAIL_TEMPLATE_NAME = "account-notification-email-template";

    private final CustomerRepository customerRepository;
    private final AccountService accountService;
    private final FinancialService financialService;
    private final MessagingService messagingService;


    @Transactional
    @Override
    public CustomerDetailDto createCustomerAccount(@NonNull final String firstName, @NonNull final String lastname, @NonNull final String email) {

        if(StringUtils.isBlank(firstName)) {
            throw new ServiceException("customer first name is required");
        }

        validateEmail(email);

        /*create customer details*/
        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastname)
                .email(email)
                .build();

        customer = customerRepository.save(customer);

        /* create system account */
        final Account account = accountService.createAccount(AccountTypeEnum.CUSTOMER, null, null);

        customer.setAccountId(account.getAccountId());
        customerRepository.save(customer);

        /*create CURRENT & SAVING financial account */
        financialService.createFinancialAccount(FinancialAccountTypeEnum.CURRENT_ACCOUNT, account.getAccountId());
        final FinancialAccount savingAccount =
                financialService.createFinancialAccount(FinancialAccountTypeEnum.SAVING_ACCOUNT, account.getAccountId());

        /* give bonus to their saving account */


        return null;
    }

    private void validateEmail(String email) {
        if(StringUtils.isBlank(email)) {
            throw new ServiceException("customer email address is required");
        }

        final boolean match = (Pattern.compile( REGEX_MATCHER_VALID_EMAIL, Pattern.CASE_INSENSITIVE ).matcher(email).matches());
        if (!match) {
            throw new ServiceException( "the email address is not valid" + email);
        }

        var existingCustomerByEmail = customerRepository.findFirstByEmail(email);
        if(existingCustomerByEmail.isPresent()) {
            throw new ServiceException("email address is already used by another customer");
        }
    }
}

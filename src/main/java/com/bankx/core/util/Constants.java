package com.bankx.core.util;

public class Constants {

    public static final String API_RESPONSE_CODE_SUCCESS = "00";

    public static final String SYSTEM_ACCOUNT_ACCOUNT_NUMBER = "000001";

    public static final double SIGNUP_BONUS_AMOUNT = 500;
    public static final double CURRENT_ACCOUNT_PAYMENT_PERCENTAGE_FEE_RATE_PERCENTAGE = 0.05;
    public static final double SAVING_ACCOUNT_CREDIT_PAYMENT_INTEREST_RATE_PERCENTAGE = 0.5;

    /* email address validation regex */
    public static final String REGEX_MATCHER_VALID_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public static final String DEFAULT_ACCOUNT_ACTIVITY_EMAIL_SUBJECT = "bank account activity notification";
    public static final String DEFAULT_EMAIL_FROM = "solomonmail88@gmail.com";
    public static final String DEFAULT_ACCOUNT_ACTIVITY_EMAIL_TEMPLATE_NAME = "account-notification-email-template";

    public static final String TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID = "customerSavingAccountId";
    public static final String TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID = "customerCurrentAccountId";
    public static final String TXN_PARAMETER_CURRENT_ACCOUNT_PAYMENT_FEE_RATE = "currentAccountPaymentFeeRate";
    public static final String TXN_PARAMETER_PAYMENT_TO_SAVING_INTEREST_RATE = "savingAccountPaymentInterestRate";
    public static final String TXN_PARAMETER_SAVING_ACCOUNT_BALANCE = "savingAccountBalance";
    public static final String TXN_PARAMETER_THIRD_PARTY_BANK_PAYABLE_ACCOUNT_ID = "bankThirdPartyBankPayableAccountId";
    public static final String TXN_PARAMETER_THIRD_PARTY_BANK_RECEIVABLE_ACCOUNT_ID = "bankThirdPartyBankReceivableAccountId";




}

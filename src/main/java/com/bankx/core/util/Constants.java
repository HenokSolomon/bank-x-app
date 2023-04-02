package com.bankx.core.util;

public class Constants {

    public static final String API_RESPONSE_CODE_SUCCESS = "00";

    public static final String SYSTEM_ACCOUNT_ACCOUNT_NUMBER = "000001";

    public static final double SIGNUP_BONUS_AMOUNT = 500;

    /* email address validation regex */
    public static final String REGEX_MATCHER_VALID_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public static final String DEFAULT_ACCOUNT_ACTIVITY_EMAIL_SUBJECT = "bank account activity notification";
    public static final String DEFAULT_EMAIL_FROM = "solomonmail88@gmail.com";
    public static final String DEFAULT_ACCOUNT_ACTIVITY_EMAIL_TEMPLATE_NAME = "account-notification-email-template";

    public static final String TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID = "customerSavingAccountId";
    public static final String TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID = "customerCurrentAccountId";



}

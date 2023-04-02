package com.bankx.core.domain.exception;

public class ServiceException extends RuntimeException {

    public static final String UNKNOWN_SERVICE_ERROR = "core_bank_01";
    public static final String INVALID_CREATE_core_bank_REQUEST = "core_bank_02";
    public static final String INVALID_EMAIL_FORMAT = "core_bank_03";
    public static final String DUPLICATE_EMAIL = "core_bank_04";
    public static final String INVALID_core_bank_RECORD = "core_bank_05";


    private String errorCode;


    public ServiceException(String message) {
        super( message );
        this.errorCode = UNKNOWN_SERVICE_ERROR;
    }


    public ServiceException(String message, String errorCode) {
        super( message );
        this.errorCode = errorCode;
    }


}

package com.bankx.core.domain.types;

import com.bankx.core.domain.journal.*;

public enum FinancialTransactionTypeEnum {

    CUSTOMER_SIGNUP_BONUS_TRANSACTION {
        @Override
        public FinancialTransactionTemplate getFinancialTransactionTemplate() {
            return new CustomerSignupBonusPaymentTransactionTemplate();
        }

    },
    CUSTOMER_TRANSFER_FROM_CURRENT_TO_SAVING_ACCOUNT_TRANSACTION {
        @Override
        public FinancialTransactionTemplate getFinancialTransactionTemplate() {
            return new CustomerTransferFromCurrentToSavingTransactionTemplate();
        }
    },
    CUSTOMER_TRANSFER_FROM_SAVING_TO_CURRENT_ACCOUNT_TRANSACTION {
        @Override
        public FinancialTransactionTemplate getFinancialTransactionTemplate() {
            return new CustomerTransferFromSavingToCurrentTransactionTemplate();
        }
    },
    BANK_CHARGE_CURRENT_ACCOUNT_FOR_PAYMENT_TRANSACTION {
        @Override
        public FinancialTransactionTemplate getFinancialTransactionTemplate() {
            return new BankChargeCurrentAccountForPaymentTransactionTemplate();
        }
    },
    BANK_ACCEPT_PAYMENT_TO_SAVING_ACCOUNT {
        @Override
        public FinancialTransactionTemplate getFinancialTransactionTemplate() {
            return new BankAcceptPaymentToCustomerSavingAccountTransactionTemplate();
        }
    };

    public abstract FinancialTransactionTemplate getFinancialTransactionTemplate();

}

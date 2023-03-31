package com.bankx.core.domain.types;

import com.bankx.core.domain.journal.CustomerSignupBonusPaymentTransactionTemplate;
import com.bankx.core.domain.journal.FinancialTransactionTemplate;

public enum FinancialTransactionTypeEnum {

    CUSTOMER_SIGNUP_BONUS_TRANSACTION {

        @Override
        public FinancialTransactionTemplate getFinancialTransactionTemplate() {
            return new CustomerSignupBonusPaymentTransactionTemplate();
        }

    };

    public abstract FinancialTransactionTemplate getFinancialTransactionTemplate();

}

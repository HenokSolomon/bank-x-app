package com.bankx.core.domain.types;

public enum FinancialAccountTypeEnum {

    CURRENT_ACCOUNT (FinancialAccountCodeTypeEnum.LIABILITY),
    SAVING_ACCOUNT (FinancialAccountCodeTypeEnum.LIABILITY),
    INTEREST_EXPENSE_ACCOUNT (FinancialAccountCodeTypeEnum.EXPENSE),
    BONUS_PAYMENT_EXPENSE_ACCOUNT(FinancialAccountCodeTypeEnum.EXPENSE),
    FEE_INCOME_ACCOUNT (FinancialAccountCodeTypeEnum.INCOME),
    REVENUE_ACCOUNT (FinancialAccountCodeTypeEnum.REVENUE),
    THIRD_PARTY_BANK_RECEIVABLE (FinancialAccountCodeTypeEnum.ASSET),
    THIRD_PARTY_BANK_PAYABLE (FinancialAccountCodeTypeEnum.LIABILITY) ;

    private FinancialAccountCodeTypeEnum accountCodeType;

    FinancialAccountTypeEnum (FinancialAccountCodeTypeEnum accountCodeType) {
        this.accountCodeType = accountCodeType;
    }

    public FinancialAccountCodeTypeEnum getAccountCodeType() {
        return accountCodeType;
    }
}

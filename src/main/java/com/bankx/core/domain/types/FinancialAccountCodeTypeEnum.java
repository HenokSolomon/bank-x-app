package com.bankx.core.domain.types;

public enum FinancialAccountCodeTypeEnum {
    ASSET(true),
    LIABILITY(false),
    EXPENSE(true),
    REVENUE(true),
    INCOME(false);

    private boolean isNormalDebit;
    FinancialAccountCodeTypeEnum(boolean isNormalDebit) {
        this.isNormalDebit = isNormalDebit;
    }

    public boolean isNormalDebit() {
        return isNormalDebit;
    }
}

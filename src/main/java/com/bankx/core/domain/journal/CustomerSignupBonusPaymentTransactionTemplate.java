package com.bankx.core.domain.journal;

import com.bankx.core.domain.entity.FinancialTransactionItem;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomerSignupBonusPaymentTransactionTemplate implements FinancialTransactionTemplate {

    @Override
    public List<FinancialTransactionItem> calculateAndGetTransactionItems(UUID financialTransactionId, double amount,
                                                                          Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccounts, Map<String, Object> currentTransactionParameter) {

        final double bonusAmount = amount;
        final UUID customerSavingAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID).toString());
        final UUID systemBonusExpenseAccountId = systemOwnedFinancialAccounts.get(FinancialAccountTypeEnum.BONUS_PAYMENT_EXPENSE_ACCOUNT);

        List<FinancialTransactionItem> financialTransactionItems = new ArrayList<>();

        /* first line credit customer saving account */
        financialTransactionItems.add( new FinancialTransactionItem(financialTransactionId, customerSavingAccountId, 0.0, bonusAmount) );

        /*2nd line recognize expense , debit system expense account */
        financialTransactionItems.add( new FinancialTransactionItem(financialTransactionId, systemBonusExpenseAccountId, bonusAmount, 0.0) );

        return financialTransactionItems;
    }

}

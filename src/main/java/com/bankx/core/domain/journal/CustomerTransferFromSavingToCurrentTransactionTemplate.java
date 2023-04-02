package com.bankx.core.domain.journal;

import com.bankx.core.domain.entity.FinancialTransactionItem;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomerTransferFromSavingToCurrentTransactionTemplate implements FinancialTransactionTemplate {

    @Override
    public List<FinancialTransactionItem> calculateAndGetTransactionItems(UUID financialTransactionId, double amount,
                                                                          Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccounts, Map<String, Object> currentTransactionParameter) {

        final double transferAmount = amount;
        final UUID customerSavingAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID).toString());
        final UUID customerCurrentAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID).toString());

        List<FinancialTransactionItem> financialTransactionItems = new ArrayList<>();

        /* first line decreases saving balance debit saving account */
        financialTransactionItems.add( new FinancialTransactionItem(1, financialTransactionId, customerSavingAccountId, transferAmount, 0d,
                "first line decreases saving balance debit saving account") );

        /*2nd line increases current balance , credit current account */
        financialTransactionItems.add( new FinancialTransactionItem(2, financialTransactionId, customerCurrentAccountId, 0d, transferAmount,
                "2nd line increases current balance , credit current account") );

        return financialTransactionItems;
    }

}

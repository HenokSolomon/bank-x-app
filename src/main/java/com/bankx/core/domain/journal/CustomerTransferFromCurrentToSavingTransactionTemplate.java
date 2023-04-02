package com.bankx.core.domain.journal;

import com.bankx.core.domain.entity.FinancialTransactionItem;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomerTransferFromCurrentToSavingTransactionTemplate implements FinancialTransactionTemplate {

    @Override
    public List<FinancialTransactionItem> calculateAndGetTransactionItems(UUID financialTransactionId, double amount,
                                                                          Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccounts, Map<String, Object> currentTransactionParameter) {


        final double transferAmount = amount;
        final UUID customerSavingAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID).toString());
        final UUID customerCurrentAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID).toString());

        List<FinancialTransactionItem> financialTransactionItems = new ArrayList<>();

        /* first line decreases current balance debit current account */
        financialTransactionItems.add( new FinancialTransactionItem(financialTransactionId, customerCurrentAccountId, transferAmount, 0.0) );

        /*2nd line increases saving balance , credit saving account */
        financialTransactionItems.add( new FinancialTransactionItem(financialTransactionId, customerSavingAccountId, 0.0, transferAmount) );

        return financialTransactionItems;
    }

}

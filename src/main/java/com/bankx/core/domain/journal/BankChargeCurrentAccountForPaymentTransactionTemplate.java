package com.bankx.core.domain.journal;

import com.bankx.core.domain.entity.FinancialTransactionItem;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BankChargeCurrentAccountForPaymentTransactionTemplate implements FinancialTransactionTemplate {

    @Override
    public List<FinancialTransactionItem> calculateAndGetTransactionItems(UUID financialTransactionId, double amount,
                                                                          Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccounts, Map<String, Object> currentTransactionParameter) {

        final double chargeAmount = amount;

        final UUID bankThirdPartyPayableAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_THIRD_PARTY_BANK_PAYABLE_ACCOUNT_ID).toString());
        final UUID customerCurrentAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_CUSTOMER_CURRENT_ACCOUNT_ID).toString());
        final UUID systemFeeIncomeAccountId = systemOwnedFinancialAccounts.get(FinancialAccountTypeEnum.FEE_INCOME_ACCOUNT);

        final double feeRatePercentage = Double.parseDouble(currentTransactionParameter.get(Constants.TXN_PARAMETER_CURRENT_ACCOUNT_PAYMENT_FEE_RATE).toString());
        /*calculate customer fee that will be debited from customer's current account*/
        double feeAmount = (amount * ( feeRatePercentage / 100d ));

        List<FinancialTransactionItem> financialTransactionItems = new ArrayList<>();

        /* first line decreases customer current balance debit current account */
        financialTransactionItems.add( new FinancialTransactionItem(1, financialTransactionId, customerCurrentAccountId, chargeAmount, 0d,
                "first line decreases customer current balance debit current account") );

        /*2nd line increases bank's 3rd party payable account , credit liability account */
        financialTransactionItems.add( new FinancialTransactionItem(2, financialTransactionId, bankThirdPartyPayableAccountId, 0d, chargeAmount,
                "2nd line increases bank's 3rd party payable account , credit liability account") );

        /* 3rd line decreases customer current balance with fee debit current account */
        financialTransactionItems.add( new FinancialTransactionItem(3, financialTransactionId, customerCurrentAccountId, feeAmount, 0d,
               "3rd line decreases customer current balance with fee debit current account" ) );

        /* 4th line increases income account by fee credit income account */
        financialTransactionItems.add( new FinancialTransactionItem(4, financialTransactionId, systemFeeIncomeAccountId, 0.0, feeAmount,
                "4th line increases income account by fee credit income account") );

        return financialTransactionItems;
    }

}

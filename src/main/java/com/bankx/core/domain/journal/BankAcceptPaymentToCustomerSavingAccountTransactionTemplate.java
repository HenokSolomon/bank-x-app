package com.bankx.core.domain.journal;

import com.bankx.core.domain.entity.FinancialTransactionItem;
import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import com.bankx.core.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BankAcceptPaymentToCustomerSavingAccountTransactionTemplate implements FinancialTransactionTemplate {

    @Override
    public List<FinancialTransactionItem> calculateAndGetTransactionItems(UUID financialTransactionId, double amount,
                                                                          Map<FinancialAccountTypeEnum, UUID> systemOwnedFinancialAccounts, Map<String, Object> currentTransactionParameter) {

        final double paymentCreditAmount = amount;

        final UUID bankThirdPartyReceivableAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_THIRD_PARTY_BANK_RECEIVABLE_ACCOUNT_ID).toString());
        final UUID customerSavingAccountId = UUID.fromString(currentTransactionParameter.get(Constants.TXN_PARAMETER_CUSTOMER_SAVING_ACCOUNT_ID).toString());
        final UUID systemInterestExpenseAccountId = systemOwnedFinancialAccounts.get(FinancialAccountTypeEnum.INTEREST_EXPENSE_ACCOUNT);

        final double interestRatePercentage = Double.parseDouble(currentTransactionParameter.get(Constants.TXN_PARAMETER_PAYMENT_TO_SAVING_INTEREST_RATE).toString());
        final double currentSavingAccountBalance = Double.parseDouble(currentTransactionParameter.get(Constants.TXN_PARAMETER_SAVING_ACCOUNT_BALANCE).toString());

        /*calculate customer fee that will be debited from customer's current account*/
        double interestAmount = ((currentSavingAccountBalance + paymentCreditAmount) * (interestRatePercentage / 100d));

        List<FinancialTransactionItem> financialTransactionItems = new ArrayList<>();

        /* first line increases customer saving balance credit saving account */
        financialTransactionItems.add( new FinancialTransactionItem(financialTransactionId, customerSavingAccountId, 0d, paymentCreditAmount) );

        /*2nd line increases bank's 3rd party receivable account , debit asset account */
        financialTransactionItems.add( new FinancialTransactionItem(financialTransactionId, bankThirdPartyReceivableAccountId, paymentCreditAmount, 0d) );

        /* 3rd line increases customer saving balance with interestAmount credit saving account */
        financialTransactionItems.add( new FinancialTransactionItem(financialTransactionId, customerSavingAccountId, 0d, interestAmount) );

        /* 4th line increases interest expenses account by interestAmount debit expense account */
        financialTransactionItems.add( new FinancialTransactionItem(financialTransactionId, systemInterestExpenseAccountId, interestAmount, 0d) );

        return financialTransactionItems;
    }

}

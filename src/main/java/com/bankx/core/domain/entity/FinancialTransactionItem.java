package com.bankx.core.domain.entity;


import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "financial_transaction_item")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransactionItem extends Audited {

    @Id
    @Column(name = "financial_transaction_item_id", columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID financialTransactionItemId;

    @Column(name = "line_number")
    private int lineNumber;

    @Column(name = "financial_transaction_id", columnDefinition = "uuid")
    private UUID financialTransactionId;

    @Column(name = "financial_account_id", columnDefinition = "uuid")
    private UUID financialAccountId;

    @Column(name = "debit_amount")
    private double debitAmount;

    @Column(name = "credit_amount")
    private double creditAmount;

    private String description;


    public FinancialTransactionItem(int lineNumber, UUID financialTransactionId, UUID financialAccountId,
                                    double debitAmount, double creditAmount, String description) {

        this.lineNumber = lineNumber;
        this.financialTransactionId = financialTransactionId;
        this.financialAccountId = financialAccountId;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.description = description;

    }

}

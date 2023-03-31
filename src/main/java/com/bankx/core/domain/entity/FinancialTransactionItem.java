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

    @Column(name = "financial_transaction_id")
    private UUID financialTransactionId;

    @Column(name = "financial_account_id")
    private UUID financialAccountId;

    @Column(name = "debit_amount")
    private double debitAmount;

    @Column(name = "credit_amount")
    private double creditAmount;

}

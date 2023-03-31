package com.bankx.core.domain.entity;


import com.bankx.core.domain.types.FinancialTransactionTypeEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "financial_transaction")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialTransaction extends Audited {
    @Id
    @Column(name = "financial_transaction_id", columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID financialTransactionId;

    @Column(name = "financial_transaction_type")
    @Enumerated(EnumType.STRING)
    private FinancialTransactionTypeEnum financialTransactionTypeEnum;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "account_id")
    private UUID accountId;

    private LocalDateTime timestamp;
    private boolean isSettled;

}

package com.bankx.core.domain.entity;


import com.bankx.core.domain.types.FinancialAccountTypeEnum;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "financial_account")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialAccount extends Audited {

    @Id
    @Column(name = "financial_account_id", columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID financialAccountId;

    @Column(name = "financial_account_type")
    @Enumerated(EnumType.STRING)
    private FinancialAccountTypeEnum financialAccountTypeEnum;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "available_balance")
    private double availableBalance;

}

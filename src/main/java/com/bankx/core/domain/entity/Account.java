package com.bankx.core.domain.entity;


import com.bankx.core.domain.types.AccountTypeEnum;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "account")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends Audited {

    @Id
    @Column(name = "account_id", columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID accountId;

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountTypeEnum;

    @Column(name = "account_number")
    private String accountNumber;

    private String principal;
    private String credential;

}

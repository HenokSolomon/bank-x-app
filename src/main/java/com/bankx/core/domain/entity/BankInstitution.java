package com.bankx.core.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "bank_institution")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankInstitution extends Audited {

    @Id
    @Column(name = "bank_institution_id", columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID bankInstitutionId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_id", columnDefinition = "uuid")
    private UUID accountId;
}

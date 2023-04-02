package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class BankInstitutionDetailDto {
    private final String bankName;
    private final String accountNumber;
}

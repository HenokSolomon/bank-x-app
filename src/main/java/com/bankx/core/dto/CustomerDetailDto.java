package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class CustomerDetailDto extends BaseResponseDto {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String accountNumber;
    private final double savingAccountBalance;
    private final double currentAccountBalance;

}

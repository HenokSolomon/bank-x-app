package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
public class CustomerDetailDto extends BaseResponseDto {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String accountNumber;
    private final double savingAccountBalance;
    private final double currentAccountBalance;

}

package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class CustomerAccountBalanceDto extends BaseResponseDto {
    private final double savingAccountBalance;
    private final double currentAccountBalance;
}

package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerAccountBalanceDto {
    private final double savingAccountBalance;
    private final double currentAccountBalance;
}

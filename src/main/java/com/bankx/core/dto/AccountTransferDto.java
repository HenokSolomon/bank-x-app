package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class AccountTransferDto extends BaseResponseDto {
    private final String transactionReference;
}

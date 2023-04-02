package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class BulkBankTransactionResponseDto extends BaseResponseDto {
    private final List<BulkBankTransactionItemResponseDto> transactionItemResponse;
}

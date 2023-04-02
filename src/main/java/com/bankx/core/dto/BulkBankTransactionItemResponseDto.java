package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BulkBankTransactionItemResponseDto {
    private String requestReference;
    private boolean isSuccess;
    private String message;
    private String transactionReference;
}

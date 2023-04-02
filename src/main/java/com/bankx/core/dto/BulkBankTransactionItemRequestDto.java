package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkBankTransactionItemRequestDto {
    private @NonNull String requestReference;
    private @NonNull BankTransactionTypeEnum bankTransactionType;
    private @NonNull String customerAccountNumber;
    private double amount;
}

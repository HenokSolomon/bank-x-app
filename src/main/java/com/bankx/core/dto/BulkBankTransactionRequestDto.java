package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkBankTransactionRequestDto {
    private @NonNull String bankInstitutionAccountNumber;
    private @NonNull List<BulkBankTransactionItemRequestDto> transactionRequests;
}

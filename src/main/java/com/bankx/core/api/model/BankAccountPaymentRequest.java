package com.bankx.core.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountPaymentRequest {
    private @NonNull String bankInstitutionAccountNumber;
    private @NonNull String customerAccountNumber;
    private double amount;
}

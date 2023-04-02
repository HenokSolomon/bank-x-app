package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class TransactionDetailDto extends BaseResponseDto {

    private final String transactionReference;
    private final String transactionType;
    private final String accountNumber;
    private final double amount;
    private final String timestamp;
    private final List<JournalEntryItem> journalEntryItems;

    @Data
    @Builder
    public static class JournalEntryItem {
        private int lineNumber;
        private String accountType;
        private double debitAmount;
        private double creditAmount;
        private String description;
    }
}
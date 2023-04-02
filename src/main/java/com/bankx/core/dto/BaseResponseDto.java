package com.bankx.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@SuperBuilder
public class BaseResponseDto {
    private String statusCode;
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
    private String message;
}

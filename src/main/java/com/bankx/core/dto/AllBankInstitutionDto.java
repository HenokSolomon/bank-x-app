package com.bankx.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class AllBankInstitutionDto extends BaseResponseDto {
    private final List<BankInstitutionDetailDto> bankInstitutions = new ArrayList<>();
}

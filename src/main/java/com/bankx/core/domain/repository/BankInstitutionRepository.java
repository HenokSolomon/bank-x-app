package com.bankx.core.domain.repository;

import com.bankx.core.domain.entity.BankInstitution;
import com.bankx.core.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankInstitutionRepository extends JpaRepository<BankInstitution, UUID> {

}

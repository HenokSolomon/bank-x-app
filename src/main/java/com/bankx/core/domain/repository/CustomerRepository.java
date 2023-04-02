package com.bankx.core.domain.repository;

import com.bankx.core.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findFirstByEmail(final String email);

    Customer findFirstByAccountId(final UUID accountId);

}

package com.costflow.accounting.domain.allocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AllocationRuleRepository {

    AllocationRule save(AllocationRule allocationRule);

    Optional<AllocationRule> findById(UUID id);

    List<AllocationRule> findAll();
}

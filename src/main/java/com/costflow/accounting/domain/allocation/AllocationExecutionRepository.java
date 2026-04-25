package com.costflow.accounting.domain.allocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AllocationExecutionRepository {

    AllocationExecution save(AllocationExecution allocationExecution);

    Optional<AllocationExecution> findById(UUID id);

    Optional<AllocationExecution> findByIdempotencyKey(String idempotencyKey);

    int countByClosingPeriodIdAndAllocationRuleId(UUID closingPeriodId, UUID allocationRuleId);

    List<AllocationExecution> findAllByClosingPeriodId(UUID closingPeriodId);
}

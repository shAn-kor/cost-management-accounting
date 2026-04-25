package com.costflow.accounting.domain.allocation;

import java.util.List;
import java.util.UUID;

public interface AllocationResultRepository {

    AllocationResult save(AllocationResult allocationResult);

    List<AllocationResult> findAllByAllocationExecutionId(UUID allocationExecutionId);

    List<AllocationResult> findAllByClosingPeriodId(UUID closingPeriodId);
}

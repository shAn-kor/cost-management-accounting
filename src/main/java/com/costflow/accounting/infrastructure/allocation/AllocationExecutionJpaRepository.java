package com.costflow.accounting.infrastructure.allocation;

import com.costflow.accounting.domain.allocation.AllocationExecution;
import com.costflow.accounting.domain.allocation.AllocationExecutionRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocationExecutionJpaRepository extends JpaRepository<AllocationExecution, UUID>, AllocationExecutionRepository {

    @Override
    Optional<AllocationExecution> findByIdempotencyKey(String idempotencyKey);

    @Override
    int countByClosingPeriodIdAndAllocationRuleId(UUID closingPeriodId, UUID allocationRuleId);

    @Override
    List<AllocationExecution> findAllByClosingPeriodId(UUID closingPeriodId);
}

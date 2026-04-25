package com.costflow.accounting.infrastructure.allocation;

import com.costflow.accounting.domain.allocation.AllocationResult;
import com.costflow.accounting.domain.allocation.AllocationResultRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AllocationResultJpaRepository extends JpaRepository<AllocationResult, UUID>, AllocationResultRepository {

    @Override
    List<AllocationResult> findAllByAllocationExecutionId(UUID allocationExecutionId);

    @Override
    @Query("select ar from AllocationResult ar where ar.allocationExecution.closingPeriod.id = :closingPeriodId")
    List<AllocationResult> findAllByClosingPeriodId(@Param("closingPeriodId") UUID closingPeriodId);
}

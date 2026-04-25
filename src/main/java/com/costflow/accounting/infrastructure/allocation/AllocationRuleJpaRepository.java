package com.costflow.accounting.infrastructure.allocation;

import com.costflow.accounting.domain.allocation.AllocationRule;
import com.costflow.accounting.domain.allocation.AllocationRuleRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocationRuleJpaRepository extends JpaRepository<AllocationRule, UUID>, AllocationRuleRepository {
}

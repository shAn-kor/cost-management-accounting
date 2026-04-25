package com.costflow.accounting.infrastructure.cost;

import com.costflow.accounting.domain.cost.CostEntry;
import com.costflow.accounting.domain.cost.CostEntryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CostEntryJpaRepository extends JpaRepository<CostEntry, UUID>, CostEntryRepository {

    @Override
    List<CostEntry> findByClosingPeriodId(UUID closingPeriodId);

    @Override
    List<CostEntry> findByClosingPeriodIdAndCostType(UUID closingPeriodId, com.costflow.accounting.domain.cost.CostType costType);

    @Override
    List<CostEntry> findByClosingPeriodIdAndCostTypeAndCostScope(
        UUID closingPeriodId,
        com.costflow.accounting.domain.cost.CostType costType,
        com.costflow.accounting.domain.cost.CostScope costScope
    );
}

package com.costflow.accounting.infrastructure.standardcost;

import com.costflow.accounting.domain.standardcost.StandardCostResult;
import com.costflow.accounting.domain.standardcost.StandardCostResultRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardCostResultJpaRepository extends JpaRepository<StandardCostResult, UUID>, StandardCostResultRepository {

    @Override
    List<StandardCostResult> findAllByClosingPeriodId(UUID closingPeriodId);

    @Override
    List<StandardCostResult> findAllByProjectId(UUID projectId);

    @Override
    int countByClosingPeriodId(UUID closingPeriodId);
}

package com.costflow.accounting.domain.standardcost;

import java.util.List;
import java.util.UUID;

public interface StandardCostResultRepository {

    StandardCostResult save(StandardCostResult standardCostResult);

    List<StandardCostResult> findAllByClosingPeriodId(UUID closingPeriodId);

    List<StandardCostResult> findAllByProjectId(UUID projectId);

    int countByClosingPeriodId(UUID closingPeriodId);
}

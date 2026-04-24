package com.costflow.accounting.domain.cost;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CostCategoryRepository {

    CostCategory save(CostCategory costCategory);

    Optional<CostCategory> findById(UUID id);

    List<CostCategory> findAll();
}

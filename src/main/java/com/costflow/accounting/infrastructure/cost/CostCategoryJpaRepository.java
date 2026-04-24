package com.costflow.accounting.infrastructure.cost;

import com.costflow.accounting.domain.cost.CostCategory;
import com.costflow.accounting.domain.cost.CostCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CostCategoryJpaRepository extends JpaRepository<CostCategory, UUID>, CostCategoryRepository {
}

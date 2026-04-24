package com.costflow.accounting.application.cost;

import com.costflow.accounting.domain.cost.CostScope;
import com.costflow.accounting.domain.cost.CostSourceType;
import com.costflow.accounting.domain.cost.CostType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateCostEntryCommand(
    UUID closingPeriodId,
    UUID costCategoryId,
    CostType costType,
    CostScope costScope,
    UUID projectId,
    UUID departmentId,
    UUID headquartersId,
    UUID employeeId,
    BigDecimal amount,
    LocalDate occurredDate,
    String description,
    CostSourceType sourceType,
    String sourceReference
) {
}

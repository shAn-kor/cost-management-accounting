package com.costflow.accounting.domain.calculation;

import java.math.BigDecimal;
import java.util.UUID;

public record CostCalculationAmounts(
    UUID projectId,
    UUID departmentId,
    UUID headquartersId,
    BigDecimal laborCostAmount,
    BigDecimal directCostAmount,
    BigDecimal allocatedCommonCostAmount,
    BigDecimal transferPriceAmount,
    BigDecimal actualCostAmount,
    BigDecimal standardCostAmount,
    BigDecimal plannedCostAmount,
    BigDecimal standardVarianceAmount,
    BigDecimal plannedVarianceAmount
) {
}

package com.costflow.accounting.domain.calculation;

import java.math.BigDecimal;

public record CostCalculationInput(
    BigDecimal laborCostAmount,
    BigDecimal directCostAmount,
    BigDecimal allocatedCommonCostAmount,
    BigDecimal transferPriceAmount,
    BigDecimal standardCostAmount,
    BigDecimal plannedCostAmount
) {
}

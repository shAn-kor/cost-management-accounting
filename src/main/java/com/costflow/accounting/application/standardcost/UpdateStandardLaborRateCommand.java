package com.costflow.accounting.application.standardcost;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateStandardLaborRateCommand(
    BigDecimal hourlyRate,
    LocalDate effectiveFrom,
    LocalDate effectiveTo
) {
}

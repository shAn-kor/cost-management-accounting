package com.costflow.accounting.application.standardcost;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateStandardLaborRateCommand(
    UUID jobGradeId,
    BigDecimal hourlyRate,
    LocalDate effectiveFrom,
    LocalDate effectiveTo
) {
}

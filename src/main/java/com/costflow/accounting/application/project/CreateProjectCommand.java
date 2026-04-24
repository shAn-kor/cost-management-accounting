package com.costflow.accounting.application.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateProjectCommand(
    UUID departmentId,
    String code,
    String name,
    String projectType,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal plannedRevenue,
    BigDecimal plannedCost
) {
}

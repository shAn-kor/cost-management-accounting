package com.costflow.accounting.application.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateProjectMemberCommand(
    UUID projectId,
    UUID employeeId,
    String role,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal allocationRate,
    BigDecimal monthlyWorkHours
) {
}

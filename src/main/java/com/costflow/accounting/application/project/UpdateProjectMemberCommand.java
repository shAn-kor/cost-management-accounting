package com.costflow.accounting.application.project;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateProjectMemberCommand(
    String role,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal allocationRate,
    BigDecimal monthlyWorkHours
) {
}

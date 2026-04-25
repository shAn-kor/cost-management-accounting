package com.costflow.accounting.domain.allocation;

import com.costflow.accounting.domain.project.Project;
import java.math.BigDecimal;

public record AllocationLine(Project project, BigDecimal basisValue, BigDecimal allocationRate, BigDecimal allocatedAmount, BigDecimal adjustmentAmount) {
}

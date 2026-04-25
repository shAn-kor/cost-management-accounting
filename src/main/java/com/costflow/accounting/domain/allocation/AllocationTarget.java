package com.costflow.accounting.domain.allocation;

import com.costflow.accounting.domain.project.Project;
import java.math.BigDecimal;

public record AllocationTarget(Project project, BigDecimal basisValue) {
}

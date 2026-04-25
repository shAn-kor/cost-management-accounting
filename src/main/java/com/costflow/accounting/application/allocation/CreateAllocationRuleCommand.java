package com.costflow.accounting.application.allocation;

import com.costflow.accounting.domain.allocation.AllocationType;
import com.costflow.accounting.domain.cost.CostScope;

public record CreateAllocationRuleCommand(String name, AllocationType allocationType, CostScope sourceScope, CostScope targetScope) {
}

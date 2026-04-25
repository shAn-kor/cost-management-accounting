package com.costflow.accounting.application.allocation;

import java.util.UUID;

public record ExecuteAllocationCommand(UUID closingPeriodId, UUID allocationRuleId, String idempotencyKey) {
}

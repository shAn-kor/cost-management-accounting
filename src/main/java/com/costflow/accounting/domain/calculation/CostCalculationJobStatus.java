package com.costflow.accounting.domain.calculation;

public enum CostCalculationJobStatus {
    REQUESTED,
    RUNNING,
    SUCCEEDED,
    FAILED,
    RETRY_WAIT,
    RECONCILE_REQUIRED,
    CANCELLED
}

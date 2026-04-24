package com.costflow.accounting.application.closing;

public record CreateClosingPeriodCommand(
    String yearMonth
) {
}

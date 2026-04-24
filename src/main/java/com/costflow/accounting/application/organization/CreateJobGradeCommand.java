package com.costflow.accounting.application.organization;

public record CreateJobGradeCommand(
    String code,
    String name,
    int sortOrder
) {
}

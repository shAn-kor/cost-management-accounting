package com.costflow.accounting.application.organization;

public record UpdateJobGradeCommand(
    String name,
    int sortOrder
) {
}

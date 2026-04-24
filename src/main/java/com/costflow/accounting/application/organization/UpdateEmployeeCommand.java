package com.costflow.accounting.application.organization;

import java.util.UUID;

public record UpdateEmployeeCommand(
    UUID departmentId,
    UUID jobGradeId,
    String name,
    String employmentType
) {
}

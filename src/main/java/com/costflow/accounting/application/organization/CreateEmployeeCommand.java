package com.costflow.accounting.application.organization;

import java.util.UUID;

public record CreateEmployeeCommand(
    UUID departmentId,
    UUID jobGradeId,
    String employeeNo,
    String name,
    String employmentType
) {
}

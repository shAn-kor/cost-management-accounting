package com.costflow.accounting.application.organization;

import java.util.UUID;

public record CreateDepartmentCommand(
    UUID headquartersId,
    String code,
    String name
) {
}

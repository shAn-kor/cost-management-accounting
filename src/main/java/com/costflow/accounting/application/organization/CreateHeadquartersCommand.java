package com.costflow.accounting.application.organization;

import java.util.UUID;

public record CreateHeadquartersCommand(
    String code,
    String name
) {
}

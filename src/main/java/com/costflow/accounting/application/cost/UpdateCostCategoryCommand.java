package com.costflow.accounting.application.cost;

public record UpdateCostCategoryCommand(
    String name,
    String categoryGroup
) {
}

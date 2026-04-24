package com.costflow.accounting.application.cost;

public record CreateCostCategoryCommand(
    String code,
    String name,
    String categoryGroup
) {
}

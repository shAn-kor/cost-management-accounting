package com.costflow.accounting.domain.allocation;

import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.domain.common.BaseEntity;
import com.costflow.accounting.domain.cost.CostScope;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "allocation_rules")
public class AllocationRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AllocationType allocationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CostScope sourceScope;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CostScope targetScope;

    @Column(nullable = false)
    private boolean active;

    protected AllocationRule() {
    }

    public AllocationRule(
        final String name,
        final AllocationType allocationType,
        final CostScope sourceScope,
        final CostScope targetScope
    ) {
        validateScope(sourceScope, targetScope);
        this.name = name;
        this.allocationType = allocationType;
        this.sourceScope = sourceScope;
        this.targetScope = targetScope;
        active = true;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AllocationType getAllocationType() {
        return allocationType;
    }

    public CostScope getSourceScope() {
        return sourceScope;
    }

    public CostScope getTargetScope() {
        return targetScope;
    }

    public boolean isActive() {
        return active;
    }

    public void update(final String name, final AllocationType allocationType, final CostScope sourceScope, final CostScope targetScope) {
        validateScope(sourceScope, targetScope);
        this.name = name;
        this.allocationType = allocationType;
        this.sourceScope = sourceScope;
        this.targetScope = targetScope;
    }

    public void changeActive(final boolean active) {
        this.active = active;
    }

    public void validateScope(final CostScope sourceScope, final CostScope targetScope) {
        if (sourceScope == CostScope.PROJECT || targetScope != CostScope.PROJECT) {
            throw new ValidationException("배부 기준은 부서/본부/전사 공통비를 프로젝트로 배부해야 합니다.");
        }
    }
}

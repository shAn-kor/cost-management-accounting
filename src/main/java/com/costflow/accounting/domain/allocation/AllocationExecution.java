package com.costflow.accounting.domain.allocation;

import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Table(
    name = "allocation_executions",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_allocation_executions_idempotency_key", columnNames = "idempotency_key"),
        @UniqueConstraint(
            name = "uk_allocation_executions_period_rule_version",
            columnNames = {"closing_period_id", "allocation_rule_id", "execution_version"}
        )
    }
)
public class AllocationExecution extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "closing_period_id", nullable = false)
    private ClosingPeriod closingPeriod;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "allocation_rule_id", nullable = false)
    private AllocationRule allocationRule;

    @Column(nullable = false)
    private int executionVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AllocationExecutionStatus status;

    @Column(length = 1000)
    private String failureReason;

    @Column(nullable = false, length = 100)
    private String idempotencyKey;

    protected AllocationExecution() {
    }

    public AllocationExecution(
        final ClosingPeriod closingPeriod,
        final AllocationRule allocationRule,
        final int executionVersion,
        final String idempotencyKey
    ) {
        this.closingPeriod = closingPeriod;
        this.allocationRule = allocationRule;
        this.executionVersion = executionVersion;
        this.idempotencyKey = idempotencyKey;
        status = AllocationExecutionStatus.REQUESTED;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public ClosingPeriod getClosingPeriod() {
        return closingPeriod;
    }

    public AllocationRule getAllocationRule() {
        return allocationRule;
    }

    public int getExecutionVersion() {
        return executionVersion;
    }

    public AllocationExecutionStatus getStatus() {
        return status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void markRunning() {
        status = AllocationExecutionStatus.RUNNING;
        failureReason = null;
    }

    public void markSucceeded() {
        status = AllocationExecutionStatus.SUCCEEDED;
        failureReason = null;
    }

    public void markFailed(final String failureReason) {
        status = AllocationExecutionStatus.FAILED;
        this.failureReason = failureReason;
    }
}

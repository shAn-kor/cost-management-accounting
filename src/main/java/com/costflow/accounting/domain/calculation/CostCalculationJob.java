package com.costflow.accounting.domain.calculation;

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
    name = "cost_calculation_jobs",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_cost_calculation_jobs_idempotency_key", columnNames = "idempotency_key"),
        @UniqueConstraint(name = "uk_cost_calculation_jobs_period_version", columnNames = {"closing_period_id", "job_version"})
    }
)
public class CostCalculationJob extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "closing_period_id", nullable = false)
    private ClosingPeriod closingPeriod;

    @Column(nullable = false)
    private int jobVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CostCalculationJobStatus status;

    @Column(nullable = false, length = 100)
    private String idempotencyKey;

    @Column(length = 1000)
    private String failureReason;

    protected CostCalculationJob() {
    }

    public CostCalculationJob(final ClosingPeriod closingPeriod, final int jobVersion, final String idempotencyKey) {
        this.closingPeriod = closingPeriod;
        this.jobVersion = jobVersion;
        this.idempotencyKey = idempotencyKey;
        status = CostCalculationJobStatus.REQUESTED;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public ClosingPeriod getClosingPeriod() {
        return closingPeriod;
    }

    public int getJobVersion() {
        return jobVersion;
    }

    public CostCalculationJobStatus getStatus() {
        return status;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void markRunning() {
        status = CostCalculationJobStatus.RUNNING;
        failureReason = null;
    }

    public void markSucceeded() {
        status = CostCalculationJobStatus.SUCCEEDED;
        failureReason = null;
    }

    public void markFailed(final String failureReason) {
        status = CostCalculationJobStatus.FAILED;
        this.failureReason = failureReason;
    }
}

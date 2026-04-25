package com.costflow.accounting.domain.allocation;

import com.costflow.accounting.domain.common.BaseEntity;
import com.costflow.accounting.domain.cost.CostEntry;
import com.costflow.accounting.domain.project.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "allocation_results")
public class AllocationResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "allocation_execution_id", nullable = false)
    private AllocationExecution allocationExecution;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_cost_entry_id", nullable = false)
    private CostEntry sourceCostEntry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_project_id", nullable = false)
    private Project targetProject;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal basisValue;

    @Column(nullable = false, precision = 12, scale = 8)
    private BigDecimal allocationRate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal adjustmentAmount;

    protected AllocationResult() {
    }

    public AllocationResult(
        final AllocationExecution allocationExecution,
        final CostEntry sourceCostEntry,
        final Project targetProject,
        final BigDecimal basisValue,
        final BigDecimal allocationRate,
        final BigDecimal allocatedAmount,
        final BigDecimal adjustmentAmount
    ) {
        this.allocationExecution = allocationExecution;
        this.sourceCostEntry = sourceCostEntry;
        this.targetProject = targetProject;
        this.basisValue = basisValue;
        this.allocationRate = allocationRate;
        this.allocatedAmount = allocatedAmount;
        this.adjustmentAmount = adjustmentAmount;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public AllocationExecution getAllocationExecution() {
        return allocationExecution;
    }

    public CostEntry getSourceCostEntry() {
        return sourceCostEntry;
    }

    public Project getTargetProject() {
        return targetProject;
    }

    public BigDecimal getBasisValue() {
        return basisValue;
    }

    public BigDecimal getAllocationRate() {
        return allocationRate;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public BigDecimal getAdjustmentAmount() {
        return adjustmentAmount;
    }
}

package com.costflow.accounting.domain.calculation;

import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.common.BaseEntity;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.Headquarters;
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
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
    name = "cost_calculation_results",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_cost_calculation_results_job_project",
        columnNames = {"cost_calculation_job_id", "project_id"}
    )
)
public class CostCalculationResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cost_calculation_job_id", nullable = false)
    private CostCalculationJob costCalculationJob;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "closing_period_id", nullable = false)
    private ClosingPeriod closingPeriod;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "headquarters_id", nullable = false)
    private Headquarters headquarters;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal laborCostAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal directCostAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal allocatedCommonCostAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal transferPriceAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal actualCostAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal standardCostAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal plannedCostAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal standardVarianceAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal plannedVarianceAmount;

    protected CostCalculationResult() {
    }

    public CostCalculationResult(
        final CostCalculationJob costCalculationJob,
        final Project project,
        final CostCalculationAmounts amounts
    ) {
        this.costCalculationJob = costCalculationJob;
        closingPeriod = costCalculationJob.getClosingPeriod();
        this.project = project;
        department = project.getDepartment();
        headquarters = project.getDepartment().getHeadquarters();
        laborCostAmount = amounts.laborCostAmount();
        directCostAmount = amounts.directCostAmount();
        allocatedCommonCostAmount = amounts.allocatedCommonCostAmount();
        transferPriceAmount = amounts.transferPriceAmount();
        actualCostAmount = amounts.actualCostAmount();
        standardCostAmount = amounts.standardCostAmount();
        plannedCostAmount = amounts.plannedCostAmount();
        standardVarianceAmount = amounts.standardVarianceAmount();
        plannedVarianceAmount = amounts.plannedVarianceAmount();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public CostCalculationJob getCostCalculationJob() {
        return costCalculationJob;
    }

    public ClosingPeriod getClosingPeriod() {
        return closingPeriod;
    }

    public Project getProject() {
        return project;
    }

    public Department getDepartment() {
        return department;
    }

    public Headquarters getHeadquarters() {
        return headquarters;
    }

    public BigDecimal getLaborCostAmount() {
        return laborCostAmount;
    }

    public BigDecimal getDirectCostAmount() {
        return directCostAmount;
    }

    public BigDecimal getAllocatedCommonCostAmount() {
        return allocatedCommonCostAmount;
    }

    public BigDecimal getTransferPriceAmount() {
        return transferPriceAmount;
    }

    public BigDecimal getActualCostAmount() {
        return actualCostAmount;
    }

    public BigDecimal getStandardCostAmount() {
        return standardCostAmount;
    }

    public BigDecimal getPlannedCostAmount() {
        return plannedCostAmount;
    }

    public BigDecimal getStandardVarianceAmount() {
        return standardVarianceAmount;
    }

    public BigDecimal getPlannedVarianceAmount() {
        return plannedVarianceAmount;
    }
}

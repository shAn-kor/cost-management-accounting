package com.costflow.accounting.domain.standardcost;

import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.common.BaseEntity;
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
    name = "standard_cost_results",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_standard_cost_results_period_project_version",
        columnNames = {"closing_period_id", "project_id", "calculation_version"}
    )
)
public class StandardCostResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "closing_period_id", nullable = false)
    private ClosingPeriod closingPeriod;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal standardLaborCost;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal standardCommonCost;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalStandardCost;

    @Column(nullable = false)
    private int calculationVersion;

    protected StandardCostResult() {
    }

    public StandardCostResult(
        final ClosingPeriod closingPeriod,
        final Project project,
        final BigDecimal standardLaborCost,
        final BigDecimal standardCommonCost,
        final int calculationVersion
    ) {
        this.closingPeriod = closingPeriod;
        this.project = project;
        this.standardLaborCost = standardLaborCost;
        this.standardCommonCost = standardCommonCost;
        this.totalStandardCost = standardLaborCost.add(standardCommonCost);
        this.calculationVersion = calculationVersion;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public ClosingPeriod getClosingPeriod() {
        return closingPeriod;
    }

    public Project getProject() {
        return project;
    }

    public BigDecimal getStandardLaborCost() {
        return standardLaborCost;
    }

    public BigDecimal getStandardCommonCost() {
        return standardCommonCost;
    }

    public BigDecimal getTotalStandardCost() {
        return totalStandardCost;
    }

    public int getCalculationVersion() {
        return calculationVersion;
    }
}

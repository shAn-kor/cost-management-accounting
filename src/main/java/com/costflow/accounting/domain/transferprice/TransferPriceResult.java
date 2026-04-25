package com.costflow.accounting.domain.transferprice;

import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.common.BaseEntity;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.standardcost.StandardLaborRate;
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
    name = "transfer_price_results",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_transfer_price_results_period_project_employee_version",
        columnNames = {"closing_period_id", "project_id", "employee_id", "calculation_version"}
    )
)
public class TransferPriceResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "closing_period_id", nullable = false)
    private ClosingPeriod closingPeriod;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "standard_labor_rate_id", nullable = false)
    private StandardLaborRate standardLaborRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal workHours;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal hourlyRate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal transferAmount;

    @Column(nullable = false)
    private int calculationVersion;

    protected TransferPriceResult() {
    }

    public TransferPriceResult(
        final ClosingPeriod closingPeriod,
        final Project project,
        final Employee employee,
        final Department department,
        final StandardLaborRate standardLaborRate,
        final BigDecimal workHours,
        final BigDecimal hourlyRate,
        final BigDecimal transferAmount,
        final int calculationVersion
    ) {
        this.closingPeriod = closingPeriod;
        this.project = project;
        this.employee = employee;
        this.department = department;
        this.standardLaborRate = standardLaborRate;
        this.workHours = workHours;
        this.hourlyRate = hourlyRate;
        this.transferAmount = transferAmount;
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

    public Employee getEmployee() {
        return employee;
    }

    public Department getDepartment() {
        return department;
    }

    public StandardLaborRate getStandardLaborRate() {
        return standardLaborRate;
    }

    public BigDecimal getWorkHours() {
        return workHours;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public int getCalculationVersion() {
        return calculationVersion;
    }
}

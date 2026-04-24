package com.costflow.accounting.domain.cost;

import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.common.BaseEntity;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.organization.Headquarters;
import com.costflow.accounting.domain.project.Project;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cost_entries")
public class CostEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "closing_period_id", nullable = false)
    private ClosingPeriod closingPeriod;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cost_category_id", nullable = false)
    private CostCategory costCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CostType costType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CostScope costScope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarters_id")
    private Headquarters headquarters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate occurredDate;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CostSourceType sourceType;

    @Column(length = 100)
    private String sourceReference;

    protected CostEntry() {
    }

    public CostEntry(
        final ClosingPeriod closingPeriod,
        final CostCategory costCategory,
        final CostType costType,
        final CostScope costScope,
        final Project project,
        final Department department,
        final Headquarters headquarters,
        final Employee employee,
        final BigDecimal amount,
        final LocalDate occurredDate,
        final String description,
        final CostSourceType sourceType,
        final String sourceReference
    ) {
        validateAmount(amount);
        validateScope(costScope, project, department, headquarters);
        this.closingPeriod = closingPeriod;
        this.costCategory = costCategory;
        this.costType = costType;
        this.costScope = costScope;
        this.project = project;
        this.department = department;
        this.headquarters = headquarters;
        this.employee = employee;
        this.amount = amount;
        this.occurredDate = occurredDate;
        this.description = description;
        this.sourceType = sourceType;
        this.sourceReference = sourceReference;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public ClosingPeriod getClosingPeriod() {
        return closingPeriod;
    }

    public CostCategory getCostCategory() {
        return costCategory;
    }

    public CostType getCostType() {
        return costType;
    }

    public CostScope getCostScope() {
        return costScope;
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

    public Employee getEmployee() {
        return employee;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getOccurredDate() {
        return occurredDate;
    }

    public String getDescription() {
        return description;
    }

    public CostSourceType getSourceType() {
        return sourceType;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void update(
        final CostCategory costCategory,
        final CostType costType,
        final CostScope costScope,
        final Project project,
        final Department department,
        final Headquarters headquarters,
        final Employee employee,
        final BigDecimal amount,
        final LocalDate occurredDate,
        final String description
    ) {
        validateAmount(amount);
        validateScope(costScope, project, department, headquarters);
        this.costCategory = costCategory;
        this.costType = costType;
        this.costScope = costScope;
        this.project = project;
        this.department = department;
        this.headquarters = headquarters;
        this.employee = employee;
        this.amount = amount;
        this.occurredDate = occurredDate;
        this.description = description;
    }

    public void validateAmount(final BigDecimal amount) {
        if (amount == null || amount.signum() < 0) {
            throw new ValidationException("비용 금액은 0 이상이어야 합니다.");
        }
    }

    public void validateScope(final CostScope costScope, final Project project, final Department department, final Headquarters headquarters) {
        final boolean invalid = switch (costScope) {
            case PROJECT -> project == null;
            case DEPARTMENT -> department == null;
            case HEADQUARTERS -> headquarters == null;
            case COMPANY -> false;
        };
        if (invalid) {
            throw new ValidationException("비용 귀속 범위에 필요한 대상 ID가 누락되었습니다.");
        }
    }
}

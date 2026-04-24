package com.costflow.accounting.domain.project;

import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.domain.common.BaseEntity;
import com.costflow.accounting.domain.organization.Department;
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
@Table(name = "projects")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 30)
    private String projectType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProjectStatus status;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal plannedRevenue;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal plannedCost;

    protected Project() {
    }

    public Project(
        final Department department,
        final String code,
        final String name,
        final String projectType,
        final LocalDate startDate,
        final LocalDate endDate,
        final BigDecimal plannedRevenue,
        final BigDecimal plannedCost
    ) {
        validatePeriod(startDate, endDate);
        this.department = department;
        this.code = code;
        this.name = name;
        this.projectType = projectType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.plannedRevenue = plannedRevenue;
        this.plannedCost = plannedCost;
        status = ProjectStatus.PLANNED;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getProjectType() {
        return projectType;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getPlannedRevenue() {
        return plannedRevenue;
    }

    public BigDecimal getPlannedCost() {
        return plannedCost;
    }

    public void update(
        final Department department,
        final String name,
        final String projectType,
        final LocalDate startDate,
        final LocalDate endDate,
        final BigDecimal plannedRevenue,
        final BigDecimal plannedCost
    ) {
        validatePeriod(startDate, endDate);
        this.department = department;
        this.name = name;
        this.projectType = projectType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.plannedRevenue = plannedRevenue;
        this.plannedCost = plannedCost;
    }

    public void changeStatus(final ProjectStatus status) {
        this.status = status;
    }

    public void validatePeriod(final LocalDate startDate, final LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ValidationException("프로젝트 종료일은 시작일보다 빠를 수 없습니다.");
        }
    }
}

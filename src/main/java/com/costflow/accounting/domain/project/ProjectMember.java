package com.costflow.accounting.domain.project;

import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.domain.common.BaseEntity;
import com.costflow.accounting.domain.organization.Employee;
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
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
    name = "project_members",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_project_members_project_employee_period",
        columnNames = {"project_id", "employee_id", "start_date", "end_date"}
    )
)
public class ProjectMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false, length = 50)
    private String role;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal allocationRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyWorkHours;

    @Column(nullable = false)
    private boolean active;

    protected ProjectMember() {
    }

    public ProjectMember(
        final Project project,
        final Employee employee,
        final String role,
        final LocalDate startDate,
        final LocalDate endDate,
        final BigDecimal allocationRate,
        final BigDecimal monthlyWorkHours
    ) {
        validate(project, startDate, endDate, allocationRate, monthlyWorkHours);
        this.project = project;
        this.employee = employee;
        this.role = role;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allocationRate = allocationRate;
        this.monthlyWorkHours = monthlyWorkHours;
        active = true;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getRole() {
        return role;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getAllocationRate() {
        return allocationRate;
    }

    public BigDecimal getMonthlyWorkHours() {
        return monthlyWorkHours;
    }

    public boolean isActive() {
        return active;
    }

    public void update(
        final String role,
        final LocalDate startDate,
        final LocalDate endDate,
        final BigDecimal allocationRate,
        final BigDecimal monthlyWorkHours
    ) {
        validate(project, startDate, endDate, allocationRate, monthlyWorkHours);
        this.role = role;
        this.startDate = startDate;
        this.endDate = endDate;
        this.allocationRate = allocationRate;
        this.monthlyWorkHours = monthlyWorkHours;
    }

    public void changeActive(final boolean active) {
        this.active = active;
    }

    public void validate(
        final Project project,
        final LocalDate startDate,
        final LocalDate endDate,
        final BigDecimal allocationRate,
        final BigDecimal monthlyWorkHours
    ) {
        if (endDate.isBefore(startDate)) {
            throw new ValidationException("프로젝트 참여 종료일은 시작일보다 빠를 수 없습니다.");
        }
        if (startDate.isBefore(project.getStartDate()) || endDate.isAfter(project.getEndDate())) {
            throw new ValidationException("프로젝트 참여 기간은 프로젝트 기간 안에 있어야 합니다.");
        }
        if (allocationRate.compareTo(BigDecimal.ZERO) <= 0 || allocationRate.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new ValidationException("투입률은 0보다 크고 100 이하이어야 합니다.");
        }
        if (monthlyWorkHours.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("월 투입시간은 음수일 수 없습니다.");
        }
    }
}

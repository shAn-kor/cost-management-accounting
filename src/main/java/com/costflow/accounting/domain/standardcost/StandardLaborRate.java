package com.costflow.accounting.domain.standardcost;

import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.domain.common.BaseEntity;
import com.costflow.accounting.domain.organization.JobGrade;
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
    name = "standard_labor_rates",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_standard_labor_rates_grade_period",
        columnNames = {"job_grade_id", "effective_from"}
    )
)
public class StandardLaborRate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_grade_id", nullable = false)
    private JobGrade jobGrade;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal hourlyRate;

    @Column(nullable = false)
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    @Column(nullable = false)
    private boolean active;

    protected StandardLaborRate() {
    }

    public StandardLaborRate(
        final JobGrade jobGrade,
        final BigDecimal hourlyRate,
        final LocalDate effectiveFrom,
        final LocalDate effectiveTo
    ) {
        validate(hourlyRate, effectiveFrom, effectiveTo);
        this.jobGrade = jobGrade;
        this.hourlyRate = hourlyRate;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        active = true;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public JobGrade getJobGrade() {
        return jobGrade;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public boolean isActive() {
        return active;
    }

    public void update(final BigDecimal hourlyRate, final LocalDate effectiveFrom, final LocalDate effectiveTo) {
        validate(hourlyRate, effectiveFrom, effectiveTo);
        this.hourlyRate = hourlyRate;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
    }

    public void changeActive(final boolean active) {
        this.active = active;
    }

    public boolean isEffectiveAt(final LocalDate date) {
        return !date.isBefore(effectiveFrom) && (effectiveTo == null || !date.isAfter(effectiveTo));
    }

    public void validate(final BigDecimal hourlyRate, final LocalDate effectiveFrom, final LocalDate effectiveTo) {
        if (hourlyRate == null || hourlyRate.signum() < 0) {
            throw new ValidationException("표준단가는 0 이상이어야 합니다.");
        }
        if (effectiveTo != null && effectiveTo.isBefore(effectiveFrom)) {
            throw new ValidationException("표준단가 적용 종료일은 시작일보다 빠를 수 없습니다.");
        }
    }
}

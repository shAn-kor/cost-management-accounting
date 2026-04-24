package com.costflow.accounting.domain.closing;

import com.costflow.accounting.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "closing_periods")
public class ClosingPeriod extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 7)
    private String yearMonth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ClosingStatus status;

    @Column(nullable = false)
    private LocalDateTime openedAt;

    private LocalDateTime calculatedAt;

    private LocalDateTime submittedAt;

    private LocalDateTime approvedAt;

    private LocalDateTime confirmedAt;

    @Version
    private long version;

    protected ClosingPeriod() {
    }

    public ClosingPeriod(final String yearMonth) {
        this.yearMonth = yearMonth;
        status = ClosingStatus.OPEN;
        openedAt = LocalDateTime.now();
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public ClosingStatus getStatus() {
        return status;
    }

    public long getVersion() {
        return version;
    }

    public void changeStatus(final ClosingStatus nextStatus, final ClosingStatePolicy policy) {
        policy.validateTransition(status, nextStatus);
        status = nextStatus;
        markTransitionTime(nextStatus);
    }

    public void markTransitionTime(final ClosingStatus nextStatus) {
        final LocalDateTime now = LocalDateTime.now();
        switch (nextStatus) {
            case CALCULATED -> calculatedAt = now;
            case SUBMITTED -> submittedAt = now;
            case APPROVED -> approvedAt = now;
            case CONFIRMED -> confirmedAt = now;
            case OPEN, REJECTED -> {
            }
        }
    }
}

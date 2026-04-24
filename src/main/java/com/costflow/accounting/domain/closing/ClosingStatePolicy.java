package com.costflow.accounting.domain.closing;

import com.costflow.accounting.application.common.InvalidStateException;
import org.springframework.stereotype.Component;

@Component
public class ClosingStatePolicy {

    public void validateCostEditable(final ClosingStatus status) {
        if (status != ClosingStatus.OPEN) {
            throw new InvalidStateException("비용은 OPEN 상태에서만 등록, 수정, 삭제할 수 있습니다.");
        }
    }

    public void validateTransition(final ClosingStatus currentStatus, final ClosingStatus nextStatus) {
        final boolean allowed = switch (currentStatus) {
            case OPEN -> nextStatus == ClosingStatus.CALCULATED;
            case CALCULATED -> nextStatus == ClosingStatus.SUBMITTED || nextStatus == ClosingStatus.OPEN;
            case SUBMITTED -> nextStatus == ClosingStatus.APPROVED || nextStatus == ClosingStatus.REJECTED;
            case APPROVED -> nextStatus == ClosingStatus.CONFIRMED;
            case REJECTED -> nextStatus == ClosingStatus.OPEN || nextStatus == ClosingStatus.CALCULATED;
            case CONFIRMED -> false;
        };
        if (!allowed) {
            throw new InvalidStateException("허용되지 않은 마감 상태 전이입니다. current=" + currentStatus + ", next=" + nextStatus);
        }
    }
}

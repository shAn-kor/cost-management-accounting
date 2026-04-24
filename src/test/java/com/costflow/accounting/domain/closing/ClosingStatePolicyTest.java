package com.costflow.accounting.domain.closing;

import com.costflow.accounting.application.common.InvalidStateException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClosingStatePolicyTest {

    private final ClosingStatePolicy policy = new ClosingStatePolicy();

    @Test
    void validateCostEditable_OPEN이면_비용수정이가능하다() {
        assertThatCode(() -> policy.validateCostEditable(ClosingStatus.OPEN))
            .doesNotThrowAnyException();
    }

    @Test
    void validateCostEditable_CONFIRMED이면_비용수정이불가능하다() {
        assertThatThrownBy(() -> policy.validateCostEditable(ClosingStatus.CONFIRMED))
            .isInstanceOf(InvalidStateException.class)
            .hasMessageContaining("OPEN 상태");
    }

    @Test
    void validateTransition_OPEN에서_CALCULATED는_허용한다() {
        assertThatCode(() -> policy.validateTransition(ClosingStatus.OPEN, ClosingStatus.CALCULATED))
            .doesNotThrowAnyException();
    }

    @Test
    void validateTransition_CONFIRMED에서_OPEN은_허용하지않는다() {
        assertThatThrownBy(() -> policy.validateTransition(ClosingStatus.CONFIRMED, ClosingStatus.OPEN))
            .isInstanceOf(InvalidStateException.class)
            .hasMessageContaining("허용되지 않은 마감 상태 전이");
    }
}

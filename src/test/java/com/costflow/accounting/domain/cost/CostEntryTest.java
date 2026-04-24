package com.costflow.accounting.domain.cost;

import com.costflow.accounting.application.common.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CostEntryTest {

    @Test
    void create_PROJECT범위인데_project가없으면_실패한다() {
        assertThatThrownBy(() -> new CostEntry(
            null,
            null,
            CostType.DIRECT,
            CostScope.PROJECT,
            null,
            null,
            null,
            null,
            BigDecimal.TEN,
            LocalDate.of(2026, 4, 1),
            "프로젝트 직접비",
            CostSourceType.MANUAL,
            null
        ))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("비용 귀속 범위");
    }

    @Test
    void create_COMPANY범위이면_대상ID없이도_생성가능하다() {
        assertThatCode(() -> new CostEntry(
            null,
            null,
            CostType.COMMON,
            CostScope.COMPANY,
            null,
            null,
            null,
            null,
            BigDecimal.TEN,
            LocalDate.of(2026, 4, 1),
            "전사 공통비",
            CostSourceType.MANUAL,
            null
        ))
            .doesNotThrowAnyException();
    }

    @Test
    void create_금액이음수이면_실패한다() {
        assertThatThrownBy(() -> new CostEntry(
            null,
            null,
            CostType.COMMON,
            CostScope.COMPANY,
            null,
            null,
            null,
            null,
            BigDecimal.valueOf(-1),
            LocalDate.of(2026, 4, 1),
            "전사 공통비",
            CostSourceType.MANUAL,
            null
        ))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("0 이상");
    }
}

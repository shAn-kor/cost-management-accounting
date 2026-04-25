package com.costflow.accounting.domain.transferprice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.costflow.accounting.application.common.ValidationException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class TransferPriceDomainServiceTest {

    private final TransferPriceDomainService transferPriceDomainService = new TransferPriceDomainService();

    @Test
    void calculate_표준단가와투입시간이있으면_내부대체가액을계산한다() {
        final BigDecimal result = transferPriceDomainService.calculate(
            BigDecimal.valueOf(100_000),
            BigDecimal.valueOf(12.5)
        );

        assertThat(result).isEqualByComparingTo("1250000.00");
    }

    @Test
    void calculate_음수투입시간이면_실패한다() {
        assertThatThrownBy(() -> transferPriceDomainService.calculate(BigDecimal.valueOf(100_000), BigDecimal.valueOf(-1)))
            .isInstanceOf(ValidationException.class);
    }
}

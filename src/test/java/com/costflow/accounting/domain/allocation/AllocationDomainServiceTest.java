package com.costflow.accounting.domain.allocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.domain.project.Project;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class AllocationDomainServiceTest {

    private final AllocationDomainService allocationDomainService = new AllocationDomainService();

    @Test
    void allocate_기준값비율대로_배부한다() {
        final Project firstProject = null;
        final Project secondProject = null;
        final List<AllocationLine> lines = allocationDomainService.allocate(
            new BigDecimal("100.00"),
            List.of(
                new AllocationTarget(firstProject, new BigDecimal("30")),
                new AllocationTarget(secondProject, new BigDecimal("70"))
            )
        );

        assertThat(lines).hasSize(2);
        assertThat(lines.get(0).allocatedAmount()).isEqualByComparingTo("30.00");
        assertThat(lines.get(1).allocatedAmount()).isEqualByComparingTo("70.00");
    }

    @Test
    void allocate_반올림오차가있으면_총액을보정한다() {
        final List<AllocationLine> lines = allocationDomainService.allocate(
            new BigDecimal("100.00"),
            List.of(
                new AllocationTarget(null, BigDecimal.ONE),
                new AllocationTarget(null, BigDecimal.ONE),
                new AllocationTarget(null, BigDecimal.ONE)
            )
        );

        final BigDecimal total = lines.stream()
            .map(AllocationLine::allocatedAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(total).isEqualByComparingTo("100.00");
        assertThat(lines.stream().map(AllocationLine::adjustmentAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
            .isEqualByComparingTo("0.01");
    }

    @Test
    void allocate_기준값합계가0이면_실패한다() {
        assertThatThrownBy(() -> allocationDomainService.allocate(
            new BigDecimal("100.00"),
            List.of(new AllocationTarget(null, BigDecimal.ZERO))
        )).isInstanceOf(ValidationException.class);
    }
}

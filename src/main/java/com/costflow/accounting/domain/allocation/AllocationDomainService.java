package com.costflow.accounting.domain.allocation;

import com.costflow.accounting.application.common.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AllocationDomainService {

    public List<AllocationLine> allocate(final BigDecimal sourceAmount, final List<AllocationTarget> targets) {
        if (sourceAmount == null || sourceAmount.signum() < 0) {
            throw new ValidationException("배부 원천 금액은 0 이상이어야 합니다.");
        }
        if (targets.isEmpty()) {
            throw new ValidationException("배부 대상 프로젝트가 없습니다.");
        }
        final BigDecimal totalBasis = targets.stream()
            .map(AllocationTarget::basisValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalBasis.signum() <= 0) {
            throw new ValidationException("배부 기준값 합계가 0이면 배부할 수 없습니다.");
        }

        final List<AllocationLine> lines = new ArrayList<>();
        BigDecimal allocatedTotal = BigDecimal.ZERO;
        for (int index = 0; index < targets.size(); index++) {
            final AllocationTarget target = targets.get(index);
            final BigDecimal allocationRate = target.basisValue().divide(totalBasis, 8, RoundingMode.HALF_UP);
            final BigDecimal allocatedAmount = sourceAmount.multiply(allocationRate).setScale(2, RoundingMode.HALF_UP);
            allocatedTotal = allocatedTotal.add(allocatedAmount);
            lines.add(new AllocationLine(target.project(), target.basisValue(), allocationRate, allocatedAmount, BigDecimal.ZERO.setScale(2)));
        }

        final BigDecimal adjustmentAmount = sourceAmount.subtract(allocatedTotal).setScale(2, RoundingMode.HALF_UP);
        if (adjustmentAmount.compareTo(BigDecimal.ZERO.setScale(2)) == 0) {
            return List.copyOf(lines);
        }
        final int adjustmentIndex = findLargestAllocatedAmountIndex(lines);
        final AllocationLine targetLine = lines.get(adjustmentIndex);
        lines.set(adjustmentIndex, new AllocationLine(
            targetLine.project(),
            targetLine.basisValue(),
            targetLine.allocationRate(),
            targetLine.allocatedAmount().add(adjustmentAmount),
            adjustmentAmount
        ));
        return List.copyOf(lines);
    }

    public int findLargestAllocatedAmountIndex(final List<AllocationLine> lines) {
        int largestIndex = 0;
        for (int index = 1; index < lines.size(); index++) {
            if (lines.get(index).allocatedAmount().compareTo(lines.get(largestIndex).allocatedAmount()) > 0) {
                largestIndex = index;
            }
        }
        return largestIndex;
    }
}

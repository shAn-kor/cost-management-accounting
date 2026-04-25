package com.costflow.accounting.domain.transferprice;

import com.costflow.accounting.application.common.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class TransferPriceDomainService {

    public BigDecimal calculate(final BigDecimal hourlyRate, final BigDecimal workHours) {
        if (hourlyRate == null || workHours == null) {
            throw new ValidationException("내부대체가액 계산에는 표준단가와 투입시간이 필요합니다.");
        }
        if (hourlyRate.signum() < 0 || workHours.signum() < 0) {
            throw new ValidationException("표준단가와 투입시간은 음수일 수 없습니다.");
        }
        return hourlyRate.multiply(workHours).setScale(2, RoundingMode.HALF_UP);
    }
}

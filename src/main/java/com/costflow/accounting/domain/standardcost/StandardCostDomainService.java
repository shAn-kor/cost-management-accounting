package com.costflow.accounting.domain.standardcost;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class StandardCostDomainService {

    public BigDecimal calculateLaborCost(final BigDecimal hourlyRate, final BigDecimal workHours) {
        return hourlyRate.multiply(workHours);
    }
}

package com.costflow.accounting.application.standardcost;

import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.standardcost.StandardCostDomainService;
import com.costflow.accounting.domain.standardcost.StandardCostResult;
import com.costflow.accounting.domain.standardcost.StandardCostResultRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StandardCostResultApplicationService {

    private final StandardCostResultRepository standardCostResultRepository;
    private final StandardCostDomainService standardCostDomainService;

    public StandardCostResultApplicationService(
        final StandardCostResultRepository standardCostResultRepository,
        final StandardCostDomainService standardCostDomainService
    ) {
        this.standardCostResultRepository = standardCostResultRepository;
        this.standardCostDomainService = standardCostDomainService;
    }

    @Transactional
    public StandardCostResult create(
        final ClosingPeriod closingPeriod,
        final Project project,
        final BigDecimal hourlyRate,
        final BigDecimal workHours,
        final BigDecimal standardCommonCost,
        final int calculationVersion
    ) {
        final BigDecimal standardLaborCost = standardCostDomainService.calculateLaborCost(hourlyRate, workHours);
        return createFromAmounts(closingPeriod, project, standardLaborCost, standardCommonCost, calculationVersion);
    }

    @Transactional
    public StandardCostResult createFromAmounts(
        final ClosingPeriod closingPeriod,
        final Project project,
        final BigDecimal standardLaborCost,
        final BigDecimal standardCommonCost,
        final int calculationVersion
    ) {
        return standardCostResultRepository.save(new StandardCostResult(
            closingPeriod,
            project,
            standardLaborCost,
            standardCommonCost,
            calculationVersion
        ));
    }

    public BigDecimal calculateLaborCost(final BigDecimal hourlyRate, final BigDecimal workHours) {
        return standardCostDomainService.calculateLaborCost(hourlyRate, workHours);
    }

    @Transactional(readOnly = true)
    public List<StandardCostResult> getByClosingPeriod(final UUID closingPeriodId) {
        return standardCostResultRepository.findAllByClosingPeriodId(closingPeriodId);
    }

    @Transactional(readOnly = true)
    public List<StandardCostResult> getByProject(final UUID projectId) {
        return standardCostResultRepository.findAllByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public int countByClosingPeriod(final UUID closingPeriodId) {
        return standardCostResultRepository.countByClosingPeriodId(closingPeriodId);
    }
}

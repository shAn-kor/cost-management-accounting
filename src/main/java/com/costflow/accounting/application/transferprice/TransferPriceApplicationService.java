package com.costflow.accounting.application.transferprice;

import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.standardcost.StandardLaborRate;
import com.costflow.accounting.domain.transferprice.TransferPriceDomainService;
import com.costflow.accounting.domain.transferprice.TransferPriceResult;
import com.costflow.accounting.domain.transferprice.TransferPriceResultRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferPriceApplicationService {

    private final TransferPriceResultRepository transferPriceResultRepository;
    private final TransferPriceDomainService transferPriceDomainService;

    public TransferPriceApplicationService(
        final TransferPriceResultRepository transferPriceResultRepository,
        final TransferPriceDomainService transferPriceDomainService
    ) {
        this.transferPriceResultRepository = transferPriceResultRepository;
        this.transferPriceDomainService = transferPriceDomainService;
    }

    @Transactional
    public TransferPriceResult create(
        final ClosingPeriod closingPeriod,
        final Project project,
        final Employee employee,
        final Department department,
        final StandardLaborRate standardLaborRate,
        final BigDecimal workHours,
        final int calculationVersion
    ) {
        final BigDecimal transferAmount = transferPriceDomainService.calculate(standardLaborRate.getHourlyRate(), workHours);
        final TransferPriceResult result = new TransferPriceResult(
            closingPeriod,
            project,
            employee,
            department,
            standardLaborRate,
            workHours,
            standardLaborRate.getHourlyRate(),
            transferAmount,
            calculationVersion
        );
        return transferPriceResultRepository.save(result);
    }

    @Transactional(readOnly = true)
    public List<TransferPriceResult> getByClosingPeriod(final UUID closingPeriodId) {
        return transferPriceResultRepository.findAllByClosingPeriodId(closingPeriodId);
    }

    @Transactional(readOnly = true)
    public List<TransferPriceResult> getByProject(final UUID projectId) {
        return transferPriceResultRepository.findAllByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public int countByClosingPeriod(final UUID closingPeriodId) {
        return transferPriceResultRepository.countByClosingPeriodId(closingPeriodId);
    }
}

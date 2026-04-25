package com.costflow.accounting.application.facade;

import com.costflow.accounting.application.closing.ClosingApplicationService;
import com.costflow.accounting.application.project.ProjectMemberApplicationService;
import com.costflow.accounting.application.standardcost.StandardLaborRateApplicationService;
import com.costflow.accounting.application.transferprice.TransferPriceApplicationService;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.project.ProjectMember;
import com.costflow.accounting.domain.standardcost.StandardLaborRate;
import com.costflow.accounting.domain.transferprice.TransferPriceResult;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransferPriceCalculationFacade {

    private final ClosingApplicationService closingApplicationService;
    private final ProjectMemberApplicationService projectMemberApplicationService;
    private final StandardLaborRateApplicationService standardLaborRateApplicationService;
    private final TransferPriceApplicationService transferPriceApplicationService;

    public TransferPriceCalculationFacade(
        final ClosingApplicationService closingApplicationService,
        final ProjectMemberApplicationService projectMemberApplicationService,
        final StandardLaborRateApplicationService standardLaborRateApplicationService,
        final TransferPriceApplicationService transferPriceApplicationService
    ) {
        this.closingApplicationService = closingApplicationService;
        this.projectMemberApplicationService = projectMemberApplicationService;
        this.standardLaborRateApplicationService = standardLaborRateApplicationService;
        this.transferPriceApplicationService = transferPriceApplicationService;
    }

    @Transactional
    public List<TransferPriceResult> calculate(final UUID closingPeriodId) {
        closingApplicationService.validateCostEditable(closingPeriodId);
        final ClosingPeriod closingPeriod = closingApplicationService.get(closingPeriodId);
        final LocalDate effectiveDate = YearMonth.parse(closingPeriod.getYearMonth()).atEndOfMonth();
        final int calculationVersion = transferPriceApplicationService.countByClosingPeriod(closingPeriodId) + 1;
        return projectMemberApplicationService.getAllActive().stream()
            .map(projectMember -> createResult(closingPeriod, effectiveDate, calculationVersion, projectMember))
            .toList();
    }

    public TransferPriceResult createResult(
        final ClosingPeriod closingPeriod,
        final LocalDate effectiveDate,
        final int calculationVersion,
        final ProjectMember projectMember
    ) {
        final StandardLaborRate standardLaborRate = standardLaborRateApplicationService.getEffectiveRate(
            projectMember.getEmployee().getJobGrade().getId(),
            effectiveDate
        );
        return transferPriceApplicationService.create(
            closingPeriod,
            projectMember.getProject(),
            projectMember.getEmployee(),
            projectMember.getEmployee().getDepartment(),
            standardLaborRate,
            projectMember.getMonthlyWorkHours(),
            calculationVersion
        );
    }
}

package com.costflow.accounting.application.facade;

import com.costflow.accounting.application.closing.ClosingApplicationService;
import com.costflow.accounting.application.project.ProjectMemberApplicationService;
import com.costflow.accounting.application.standardcost.StandardCostResultApplicationService;
import com.costflow.accounting.application.standardcost.StandardLaborRateApplicationService;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.project.ProjectMember;
import com.costflow.accounting.domain.standardcost.StandardCostResult;
import com.costflow.accounting.domain.standardcost.StandardLaborRate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StandardCostCalculationFacade {

    private final ClosingApplicationService closingApplicationService;
    private final ProjectMemberApplicationService projectMemberApplicationService;
    private final StandardLaborRateApplicationService standardLaborRateApplicationService;
    private final StandardCostResultApplicationService standardCostResultApplicationService;

    public StandardCostCalculationFacade(
        final ClosingApplicationService closingApplicationService,
        final ProjectMemberApplicationService projectMemberApplicationService,
        final StandardLaborRateApplicationService standardLaborRateApplicationService,
        final StandardCostResultApplicationService standardCostResultApplicationService
    ) {
        this.closingApplicationService = closingApplicationService;
        this.projectMemberApplicationService = projectMemberApplicationService;
        this.standardLaborRateApplicationService = standardLaborRateApplicationService;
        this.standardCostResultApplicationService = standardCostResultApplicationService;
    }

    @Transactional
    public List<StandardCostResult> calculate(final UUID closingPeriodId) {
        closingApplicationService.validateCostEditable(closingPeriodId);
        final ClosingPeriod closingPeriod = closingApplicationService.get(closingPeriodId);
        final LocalDate effectiveDate = YearMonth.parse(closingPeriod.getYearMonth()).atEndOfMonth();
        final int calculationVersion = standardCostResultApplicationService.countByClosingPeriod(closingPeriodId) + 1;
        return projectMemberApplicationService.getAllActive().stream()
            .collect(Collectors.groupingBy(ProjectMember::getProject))
            .entrySet()
            .stream()
            .map(entry -> createResult(closingPeriod, effectiveDate, calculationVersion, entry))
            .toList();
    }

    public StandardCostResult createResult(
        final ClosingPeriod closingPeriod,
        final LocalDate effectiveDate,
        final int calculationVersion,
        final Map.Entry<Project, List<ProjectMember>> projectMembersEntry
    ) {
        final BigDecimal standardLaborCost = projectMembersEntry.getValue().stream()
            .map(projectMember -> calculateMemberLaborCost(projectMember, effectiveDate))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return standardCostResultApplicationService.createFromAmounts(
            closingPeriod,
            projectMembersEntry.getKey(),
            standardLaborCost,
            BigDecimal.ZERO,
            calculationVersion
        );
    }

    public BigDecimal calculateMemberLaborCost(final ProjectMember projectMember, final LocalDate effectiveDate) {
        final StandardLaborRate standardLaborRate = standardLaborRateApplicationService.getEffectiveRate(
            projectMember.getEmployee().getJobGrade().getId(),
            effectiveDate
        );
        return standardCostResultApplicationService.calculateLaborCost(
            standardLaborRate.getHourlyRate(),
            projectMember.getMonthlyWorkHours()
        );
    }
}


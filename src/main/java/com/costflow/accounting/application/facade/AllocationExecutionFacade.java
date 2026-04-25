package com.costflow.accounting.application.facade;

import com.costflow.accounting.application.allocation.AllocationApplicationService;
import com.costflow.accounting.application.allocation.ExecuteAllocationCommand;
import com.costflow.accounting.application.closing.ClosingApplicationService;
import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.application.cost.CostEntryApplicationService;
import com.costflow.accounting.application.project.ProjectApplicationService;
import com.costflow.accounting.application.project.ProjectMemberApplicationService;
import com.costflow.accounting.domain.allocation.AllocationDomainService;
import com.costflow.accounting.domain.allocation.AllocationExecution;
import com.costflow.accounting.domain.allocation.AllocationLine;
import com.costflow.accounting.domain.allocation.AllocationRule;
import com.costflow.accounting.domain.allocation.AllocationTarget;
import com.costflow.accounting.domain.allocation.AllocationType;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.cost.CostEntry;
import com.costflow.accounting.domain.cost.CostScope;
import com.costflow.accounting.domain.cost.CostType;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.project.ProjectMember;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AllocationExecutionFacade {

    private final ClosingApplicationService closingApplicationService;
    private final CostEntryApplicationService costEntryApplicationService;
    private final ProjectApplicationService projectApplicationService;
    private final ProjectMemberApplicationService projectMemberApplicationService;
    private final AllocationApplicationService allocationApplicationService;
    private final AllocationDomainService allocationDomainService;

    public AllocationExecutionFacade(
        final ClosingApplicationService closingApplicationService,
        final CostEntryApplicationService costEntryApplicationService,
        final ProjectApplicationService projectApplicationService,
        final ProjectMemberApplicationService projectMemberApplicationService,
        final AllocationApplicationService allocationApplicationService,
        final AllocationDomainService allocationDomainService
    ) {
        this.closingApplicationService = closingApplicationService;
        this.costEntryApplicationService = costEntryApplicationService;
        this.projectApplicationService = projectApplicationService;
        this.projectMemberApplicationService = projectMemberApplicationService;
        this.allocationApplicationService = allocationApplicationService;
        this.allocationDomainService = allocationDomainService;
    }

    public AllocationExecution execute(final ExecuteAllocationCommand command) {
        final String idempotencyKey = command.idempotencyKey();
        final var existingExecution = allocationApplicationService.getExecutionByIdempotencyKey(idempotencyKey);
        if (existingExecution.isPresent()) {
            return existingExecution.get();
        }

        closingApplicationService.validateCostEditable(command.closingPeriodId());
        final ClosingPeriod closingPeriod = closingApplicationService.get(command.closingPeriodId());
        final AllocationRule allocationRule = allocationApplicationService.getRule(command.allocationRuleId());
        final List<CostEntry> commonCosts = costEntryApplicationService.getByClosingPeriodAndCostTypeAndCostScope(
            closingPeriod.getId(),
            CostType.COMMON,
            allocationRule.getSourceScope()
        );
        if (commonCosts.isEmpty()) {
            throw new ValidationException("배부할 공통비가 없습니다.");
        }

        final AllocationExecution execution = allocationApplicationService.createExecution(closingPeriod, allocationRule, idempotencyKey);
        allocationApplicationService.markRunning(execution.getId());
        try {
            for (CostEntry commonCost : commonCosts) {
                final List<AllocationTarget> targets = buildTargets(allocationRule, commonCost);
                final List<AllocationLine> lines = allocationDomainService.allocate(commonCost.getAmount(), targets);
                lines.forEach(line -> allocationApplicationService.createResult(execution, commonCost, line));
            }
            return allocationApplicationService.markSucceeded(execution.getId());
        } catch (RuntimeException exception) {
            allocationApplicationService.markFailed(execution.getId(), exception.getMessage());
            throw exception;
        }
    }

    public List<AllocationTarget> buildTargets(final AllocationRule allocationRule, final CostEntry commonCost) {
        final List<Project> targetProjects = switch (allocationRule.getSourceScope()) {
            case DEPARTMENT -> projectApplicationService.getByDepartment(commonCost.getDepartment().getId());
            case HEADQUARTERS -> projectApplicationService.getByHeadquarters(commonCost.getHeadquarters().getId());
            case COMPANY -> projectApplicationService.getAll();
            case PROJECT -> throw new ValidationException("프로젝트 범위 비용은 공통비 배부 대상이 아닙니다.");
        };
        if (targetProjects.isEmpty()) {
            throw new ValidationException("배부 대상 프로젝트가 없습니다.");
        }
        return targetProjects.stream()
            .map(project -> new AllocationTarget(project, basisValue(allocationRule.getAllocationType(), project, commonCost.getClosingPeriod().getId())))
            .toList();
    }

    public BigDecimal basisValue(final AllocationType allocationType, final Project project, final java.util.UUID closingPeriodId) {
        return switch (allocationType) {
            case HOURS -> projectMemberApplicationService.getAllByProjectId(project.getId()).stream()
                .filter(ProjectMember::isActive)
                .map(ProjectMember::getMonthlyWorkHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            case DIRECT_COST -> costEntryApplicationService.getByClosingPeriodAndCostType(closingPeriodId, CostType.DIRECT).stream()
                .filter(costEntry -> costEntry.getProject() != null && costEntry.getProject().getId().equals(project.getId()))
                .map(CostEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            case EQUAL -> BigDecimal.ONE;
        };
    }
}

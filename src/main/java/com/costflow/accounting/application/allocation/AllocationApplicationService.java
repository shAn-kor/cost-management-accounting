package com.costflow.accounting.application.allocation;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.allocation.AllocationExecution;
import com.costflow.accounting.domain.allocation.AllocationExecutionRepository;
import com.costflow.accounting.domain.allocation.AllocationLine;
import com.costflow.accounting.domain.allocation.AllocationResult;
import com.costflow.accounting.domain.allocation.AllocationResultRepository;
import com.costflow.accounting.domain.allocation.AllocationRule;
import com.costflow.accounting.domain.allocation.AllocationRuleRepository;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.cost.CostEntry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AllocationApplicationService {

    private final AllocationRuleRepository allocationRuleRepository;
    private final AllocationExecutionRepository allocationExecutionRepository;
    private final AllocationResultRepository allocationResultRepository;

    public AllocationApplicationService(
        final AllocationRuleRepository allocationRuleRepository,
        final AllocationExecutionRepository allocationExecutionRepository,
        final AllocationResultRepository allocationResultRepository
    ) {
        this.allocationRuleRepository = allocationRuleRepository;
        this.allocationExecutionRepository = allocationExecutionRepository;
        this.allocationResultRepository = allocationResultRepository;
    }

    @Transactional
    public AllocationRule createRule(final CreateAllocationRuleCommand command) {
        return allocationRuleRepository.save(new AllocationRule(
            command.name(),
            command.allocationType(),
            command.sourceScope(),
            command.targetScope()
        ));
    }

    @Transactional
    public AllocationRule updateRule(final UUID allocationRuleId, final UpdateAllocationRuleCommand command) {
        final AllocationRule allocationRule = getRule(allocationRuleId);
        allocationRule.update(command.name(), command.allocationType(), command.sourceScope(), command.targetScope());
        return allocationRule;
    }

    @Transactional
    public AllocationRule changeRuleActive(final UUID allocationRuleId, final boolean active) {
        final AllocationRule allocationRule = getRule(allocationRuleId);
        allocationRule.changeActive(active);
        return allocationRule;
    }

    @Transactional(readOnly = true)
    public AllocationRule getRule(final UUID allocationRuleId) {
        return allocationRuleRepository.findById(allocationRuleId)
            .orElseThrow(() -> new NotFoundException("배부 기준을 찾을 수 없습니다. id=" + allocationRuleId));
    }

    @Transactional(readOnly = true)
    public List<AllocationRule> getRules() {
        return allocationRuleRepository.findAll();
    }

    @Transactional
    public AllocationExecution createExecution(final ClosingPeriod closingPeriod, final AllocationRule allocationRule, final String idempotencyKey) {
        final int executionVersion = allocationExecutionRepository.countByClosingPeriodIdAndAllocationRuleId(
            closingPeriod.getId(),
            allocationRule.getId()
        ) + 1;
        final AllocationExecution allocationExecution = new AllocationExecution(closingPeriod, allocationRule, executionVersion, idempotencyKey);
        return allocationExecutionRepository.save(allocationExecution);
    }

    @Transactional
    public AllocationExecution markRunning(final UUID allocationExecutionId) {
        final AllocationExecution allocationExecution = getExecution(allocationExecutionId);
        allocationExecution.markRunning();
        return allocationExecution;
    }

    @Transactional
    public AllocationExecution markSucceeded(final UUID allocationExecutionId) {
        final AllocationExecution allocationExecution = getExecution(allocationExecutionId);
        allocationExecution.markSucceeded();
        return allocationExecution;
    }

    @Transactional
    public AllocationExecution markFailed(final UUID allocationExecutionId, final String failureReason) {
        final AllocationExecution allocationExecution = getExecution(allocationExecutionId);
        allocationExecution.markFailed(failureReason);
        return allocationExecution;
    }

    @Transactional
    public AllocationResult createResult(
        final AllocationExecution allocationExecution,
        final CostEntry sourceCostEntry,
        final AllocationLine allocationLine
    ) {
        return allocationResultRepository.save(new AllocationResult(
            allocationExecution,
            sourceCostEntry,
            allocationLine.project(),
            allocationLine.basisValue(),
            allocationLine.allocationRate(),
            allocationLine.allocatedAmount(),
            allocationLine.adjustmentAmount()
        ));
    }

    @Transactional(readOnly = true)
    public AllocationExecution getExecution(final UUID allocationExecutionId) {
        return allocationExecutionRepository.findById(allocationExecutionId)
            .orElseThrow(() -> new NotFoundException("배부 실행을 찾을 수 없습니다. id=" + allocationExecutionId));
    }

    @Transactional(readOnly = true)
    public Optional<AllocationExecution> getExecutionByIdempotencyKey(final String idempotencyKey) {
        return allocationExecutionRepository.findByIdempotencyKey(idempotencyKey);
    }

    @Transactional(readOnly = true)
    public List<AllocationExecution> getExecutionsByClosingPeriod(final UUID closingPeriodId) {
        return allocationExecutionRepository.findAllByClosingPeriodId(closingPeriodId);
    }

    @Transactional(readOnly = true)
    public List<AllocationResult> getResults(final UUID allocationExecutionId) {
        return allocationResultRepository.findAllByAllocationExecutionId(allocationExecutionId);
    }

    @Transactional(readOnly = true)
    public List<AllocationResult> getResultsByClosingPeriod(final UUID closingPeriodId) {
        return allocationResultRepository.findAllByClosingPeriodId(closingPeriodId);
    }
}

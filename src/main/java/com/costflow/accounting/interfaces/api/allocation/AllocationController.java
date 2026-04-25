package com.costflow.accounting.interfaces.api.allocation;

import com.costflow.accounting.application.allocation.AllocationApplicationService;
import com.costflow.accounting.application.allocation.CreateAllocationRuleCommand;
import com.costflow.accounting.application.allocation.ExecuteAllocationCommand;
import com.costflow.accounting.application.allocation.UpdateAllocationRuleCommand;
import com.costflow.accounting.application.facade.AllocationExecutionFacade;
import com.costflow.accounting.domain.allocation.AllocationExecution;
import com.costflow.accounting.domain.allocation.AllocationExecutionStatus;
import com.costflow.accounting.domain.allocation.AllocationResult;
import com.costflow.accounting.domain.allocation.AllocationRule;
import com.costflow.accounting.domain.allocation.AllocationType;
import com.costflow.accounting.domain.cost.CostScope;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AllocationController {

    private final AllocationApplicationService allocationApplicationService;
    private final AllocationExecutionFacade allocationExecutionFacade;

    public AllocationController(
        final AllocationApplicationService allocationApplicationService,
        final AllocationExecutionFacade allocationExecutionFacade
    ) {
        this.allocationApplicationService = allocationApplicationService;
        this.allocationExecutionFacade = allocationExecutionFacade;
    }

    @PostMapping("/allocation-rules")
    @ResponseStatus(HttpStatus.CREATED)
    public AllocationRuleResponse createRule(@Valid @RequestBody final AllocationRuleRequest request) {
        return AllocationRuleResponse.from(allocationApplicationService.createRule(new CreateAllocationRuleCommand(
            request.name(),
            request.allocationType(),
            request.sourceScope(),
            CostScope.PROJECT
        )));
    }

    @GetMapping("/allocation-rules")
    public List<AllocationRuleResponse> getRules() {
        return allocationApplicationService.getRules().stream()
            .map(AllocationRuleResponse::from)
            .toList();
    }

    @PutMapping("/allocation-rules/{allocationRuleId}")
    public AllocationRuleResponse updateRule(
        @PathVariable final UUID allocationRuleId,
        @Valid @RequestBody final AllocationRuleRequest request
    ) {
        return AllocationRuleResponse.from(allocationApplicationService.updateRule(
            allocationRuleId,
            new UpdateAllocationRuleCommand(request.name(), request.allocationType(), request.sourceScope(), CostScope.PROJECT)
        ));
    }

    @PatchMapping("/allocation-rules/{allocationRuleId}/active")
    public AllocationRuleResponse changeRuleActive(
        @PathVariable final UUID allocationRuleId,
        @Valid @RequestBody final ChangeActiveRequest request
    ) {
        return AllocationRuleResponse.from(allocationApplicationService.changeRuleActive(allocationRuleId, request.active()));
    }

    @PostMapping("/closing-periods/{closingPeriodId}/allocation-executions")
    @ResponseStatus(HttpStatus.CREATED)
    public AllocationExecutionResponse execute(
        @PathVariable final UUID closingPeriodId,
        @Valid @RequestBody final ExecuteAllocationRequest request,
        @RequestHeader(name = "Idempotency-Key", required = false) final String idempotencyKeyHeader
    ) {
        final String idempotencyKey = idempotencyKeyHeader == null || idempotencyKeyHeader.isBlank()
            ? request.idempotencyKey()
            : idempotencyKeyHeader;
        return AllocationExecutionResponse.from(allocationExecutionFacade.execute(new ExecuteAllocationCommand(
            closingPeriodId,
            request.allocationRuleId(),
            idempotencyKey
        )));
    }

    @GetMapping("/allocation-executions/{allocationExecutionId}")
    public AllocationExecutionResponse getExecution(@PathVariable final UUID allocationExecutionId) {
        return AllocationExecutionResponse.from(allocationApplicationService.getExecution(allocationExecutionId));
    }

    @GetMapping("/allocation-executions/{allocationExecutionId}/results")
    public List<AllocationResultResponse> getResults(@PathVariable final UUID allocationExecutionId) {
        return allocationApplicationService.getResults(allocationExecutionId).stream()
            .map(AllocationResultResponse::from)
            .toList();
    }

    public record AllocationRuleRequest(
        @NotBlank String name,
        @NotNull AllocationType allocationType,
        @NotNull CostScope sourceScope
    ) {
    }

    public record ExecuteAllocationRequest(@NotNull UUID allocationRuleId, @NotBlank String idempotencyKey) {
    }

    public record ChangeActiveRequest(boolean active) {
    }

    public record AllocationRuleResponse(
        UUID id,
        String name,
        AllocationType allocationType,
        CostScope sourceScope,
        CostScope targetScope,
        boolean active
    ) {
        public static AllocationRuleResponse from(final AllocationRule allocationRule) {
            return new AllocationRuleResponse(
                allocationRule.getId(),
                allocationRule.getName(),
                allocationRule.getAllocationType(),
                allocationRule.getSourceScope(),
                allocationRule.getTargetScope(),
                allocationRule.isActive()
            );
        }
    }

    public record AllocationExecutionResponse(
        UUID id,
        UUID closingPeriodId,
        UUID allocationRuleId,
        int executionVersion,
        AllocationExecutionStatus status,
        String failureReason,
        String idempotencyKey
    ) {
        public static AllocationExecutionResponse from(final AllocationExecution allocationExecution) {
            return new AllocationExecutionResponse(
                allocationExecution.getId(),
                allocationExecution.getClosingPeriod().getId(),
                allocationExecution.getAllocationRule().getId(),
                allocationExecution.getExecutionVersion(),
                allocationExecution.getStatus(),
                allocationExecution.getFailureReason(),
                allocationExecution.getIdempotencyKey()
            );
        }
    }

    public record AllocationResultResponse(
        UUID id,
        UUID allocationExecutionId,
        UUID sourceCostEntryId,
        UUID targetProjectId,
        BigDecimal basisValue,
        BigDecimal allocationRate,
        BigDecimal allocatedAmount,
        BigDecimal adjustmentAmount
    ) {
        public static AllocationResultResponse from(final AllocationResult allocationResult) {
            return new AllocationResultResponse(
                allocationResult.getId(),
                allocationResult.getAllocationExecution().getId(),
                allocationResult.getSourceCostEntry().getId(),
                allocationResult.getTargetProject().getId(),
                allocationResult.getBasisValue(),
                allocationResult.getAllocationRate(),
                allocationResult.getAllocatedAmount(),
                allocationResult.getAdjustmentAmount()
            );
        }
    }
}

package com.costflow.accounting.interfaces.api.standardcost;

import com.costflow.accounting.application.facade.StandardCostCalculationFacade;
import com.costflow.accounting.application.standardcost.CreateStandardLaborRateCommand;
import com.costflow.accounting.application.standardcost.StandardCostResultApplicationService;
import com.costflow.accounting.application.standardcost.StandardLaborRateApplicationService;
import com.costflow.accounting.application.standardcost.UpdateStandardLaborRateCommand;
import com.costflow.accounting.domain.standardcost.StandardCostResult;
import com.costflow.accounting.domain.standardcost.StandardLaborRate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StandardLaborRateController {

    private final StandardLaborRateApplicationService standardLaborRateApplicationService;
    private final StandardCostResultApplicationService standardCostResultApplicationService;
    private final StandardCostCalculationFacade standardCostCalculationFacade;

    public StandardLaborRateController(
        final StandardLaborRateApplicationService standardLaborRateApplicationService,
        final StandardCostResultApplicationService standardCostResultApplicationService,
        final StandardCostCalculationFacade standardCostCalculationFacade
    ) {
        this.standardLaborRateApplicationService = standardLaborRateApplicationService;
        this.standardCostResultApplicationService = standardCostResultApplicationService;
        this.standardCostCalculationFacade = standardCostCalculationFacade;
    }

    @PostMapping("/standard-labor-rates")
    @ResponseStatus(HttpStatus.CREATED)
    public StandardLaborRateResponse create(@Valid @RequestBody final CreateStandardLaborRateRequest request) {
        final StandardLaborRate standardLaborRate = standardLaborRateApplicationService.create(new CreateStandardLaborRateCommand(
            request.jobGradeId(),
            request.hourlyRate(),
            request.effectiveFrom(),
            request.effectiveTo()
        ));
        return StandardLaborRateResponse.from(standardLaborRate);
    }

    @GetMapping("/standard-labor-rates")
    public List<StandardLaborRateResponse> getAll() {
        return standardLaborRateApplicationService.getAll().stream()
            .map(StandardLaborRateResponse::from)
            .toList();
    }

    @PutMapping("/standard-labor-rates/{standardLaborRateId}")
    public StandardLaborRateResponse update(
        @PathVariable final UUID standardLaborRateId,
        @Valid @RequestBody final UpdateStandardLaborRateRequest request
    ) {
        final StandardLaborRate standardLaborRate = standardLaborRateApplicationService.update(
            standardLaborRateId,
            new UpdateStandardLaborRateCommand(request.hourlyRate(), request.effectiveFrom(), request.effectiveTo())
        );
        return StandardLaborRateResponse.from(standardLaborRate);
    }

    @PatchMapping("/standard-labor-rates/{standardLaborRateId}/active")
    public StandardLaborRateResponse changeActive(
        @PathVariable final UUID standardLaborRateId,
        @Valid @RequestBody final ChangeActiveRequest request
    ) {
        final StandardLaborRate standardLaborRate = standardLaborRateApplicationService.changeActive(standardLaborRateId, request.active());
        return StandardLaborRateResponse.from(standardLaborRate);
    }

    @PostMapping("/closing-periods/{closingPeriodId}/standard-costs/calculate")
    @ResponseStatus(HttpStatus.CREATED)
    public List<StandardCostResultResponse> calculateStandardCosts(@PathVariable final UUID closingPeriodId) {
        return standardCostCalculationFacade.calculate(closingPeriodId).stream()
            .map(StandardCostResultResponse::from)
            .toList();
    }

    @GetMapping("/closing-periods/{closingPeriodId}/standard-costs")
    public List<StandardCostResultResponse> getStandardCostsByClosingPeriod(@PathVariable final UUID closingPeriodId) {
        return standardCostResultApplicationService.getByClosingPeriod(closingPeriodId).stream()
            .map(StandardCostResultResponse::from)
            .toList();
    }

    @GetMapping("/projects/{projectId}/standard-costs")
    public List<StandardCostResultResponse> getStandardCostsByProject(@PathVariable final UUID projectId) {
        return standardCostResultApplicationService.getByProject(projectId).stream()
            .map(StandardCostResultResponse::from)
            .toList();
    }

    public record CreateStandardLaborRateRequest(
        @NotNull UUID jobGradeId,
        @NotNull @PositiveOrZero BigDecimal hourlyRate,
        @NotNull LocalDate effectiveFrom,
        LocalDate effectiveTo
    ) {
    }

    public record UpdateStandardLaborRateRequest(
        @NotNull @PositiveOrZero BigDecimal hourlyRate,
        @NotNull LocalDate effectiveFrom,
        LocalDate effectiveTo
    ) {
    }

    public record ChangeActiveRequest(boolean active) {
    }

    public record StandardCostResultResponse(
        UUID id,
        UUID closingPeriodId,
        UUID projectId,
        BigDecimal standardLaborCost,
        BigDecimal standardCommonCost,
        BigDecimal totalStandardCost,
        int calculationVersion
    ) {
        public static StandardCostResultResponse from(final StandardCostResult standardCostResult) {
            return new StandardCostResultResponse(
                standardCostResult.getId(),
                standardCostResult.getClosingPeriod().getId(),
                standardCostResult.getProject().getId(),
                standardCostResult.getStandardLaborCost(),
                standardCostResult.getStandardCommonCost(),
                standardCostResult.getTotalStandardCost(),
                standardCostResult.getCalculationVersion()
            );
        }
    }

    public record StandardLaborRateResponse(
        UUID id,
        UUID jobGradeId,
        BigDecimal hourlyRate,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        boolean active
    ) {
        public static StandardLaborRateResponse from(final StandardLaborRate standardLaborRate) {
            return new StandardLaborRateResponse(
                standardLaborRate.getId(),
                standardLaborRate.getJobGrade().getId(),
                standardLaborRate.getHourlyRate(),
                standardLaborRate.getEffectiveFrom(),
                standardLaborRate.getEffectiveTo(),
                standardLaborRate.isActive()
            );
        }
    }
}

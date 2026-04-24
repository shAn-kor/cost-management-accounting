package com.costflow.accounting.interfaces.api.cost;

import com.costflow.accounting.application.cost.CreateCostEntryCommand;
import com.costflow.accounting.application.cost.UpdateCostEntryCommand;
import com.costflow.accounting.application.facade.CostEntryRegistrationFacade;
import com.costflow.accounting.domain.cost.CostEntry;
import com.costflow.accounting.domain.cost.CostScope;
import com.costflow.accounting.domain.cost.CostSourceType;
import com.costflow.accounting.domain.cost.CostType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CostEntryController {

    private final CostEntryRegistrationFacade costEntryRegistrationFacade;

    public CostEntryController(final CostEntryRegistrationFacade costEntryRegistrationFacade) {
        this.costEntryRegistrationFacade = costEntryRegistrationFacade;
    }

    @PostMapping("/closing-periods/{closingPeriodId}/cost-entries")
    @ResponseStatus(HttpStatus.CREATED)
    public CostEntryResponse create(
        @PathVariable final UUID closingPeriodId,
        @Valid @RequestBody final CreateCostEntryRequest request
    ) {
        final CostEntry costEntry = costEntryRegistrationFacade.create(new CreateCostEntryCommand(
            closingPeriodId,
            request.costCategoryId(),
            request.costType(),
            request.costScope(),
            request.projectId(),
            request.departmentId(),
            request.headquartersId(),
            request.employeeId(),
            request.amount(),
            request.occurredDate(),
            request.description(),
            request.sourceType(),
            request.sourceReference()
        ));
        return CostEntryResponse.from(costEntry);
    }

    @GetMapping("/cost-entries/{costEntryId}")
    public CostEntryResponse get(@PathVariable final UUID costEntryId) {
        return CostEntryResponse.from(costEntryRegistrationFacade.get(costEntryId));
    }

    @GetMapping("/closing-periods/{closingPeriodId}/cost-entries")
    public List<CostEntryResponse> getByClosingPeriod(@PathVariable final UUID closingPeriodId) {
        return costEntryRegistrationFacade.getByClosingPeriod(closingPeriodId).stream()
            .map(CostEntryResponse::from)
            .toList();
    }

    @PutMapping("/cost-entries/{costEntryId}")
    public CostEntryResponse update(
        @PathVariable final UUID costEntryId,
        @Valid @RequestBody final UpdateCostEntryRequest request
    ) {
        final CostEntry costEntry = costEntryRegistrationFacade.update(new UpdateCostEntryCommand(
            costEntryId,
            request.costCategoryId(),
            request.costType(),
            request.costScope(),
            request.projectId(),
            request.departmentId(),
            request.headquartersId(),
            request.employeeId(),
            request.amount(),
            request.occurredDate(),
            request.description()
        ));
        return CostEntryResponse.from(costEntry);
    }

    @DeleteMapping("/cost-entries/{costEntryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final UUID costEntryId) {
        costEntryRegistrationFacade.delete(costEntryId);
    }

    public record CreateCostEntryRequest(
        @NotNull UUID costCategoryId,
        @NotNull CostType costType,
        @NotNull CostScope costScope,
        UUID projectId,
        UUID departmentId,
        UUID headquartersId,
        UUID employeeId,
        @NotNull @PositiveOrZero BigDecimal amount,
        @NotNull LocalDate occurredDate,
        @NotNull @Size(max = 500) String description,
        @NotNull CostSourceType sourceType,
        @Size(max = 100) String sourceReference
    ) {
    }

    public record UpdateCostEntryRequest(
        @NotNull UUID costCategoryId,
        @NotNull CostType costType,
        @NotNull CostScope costScope,
        UUID projectId,
        UUID departmentId,
        UUID headquartersId,
        UUID employeeId,
        @NotNull @PositiveOrZero BigDecimal amount,
        @NotNull LocalDate occurredDate,
        @NotNull @Size(max = 500) String description
    ) {
    }

    public record CostEntryResponse(
        UUID id,
        UUID closingPeriodId,
        UUID costCategoryId,
        CostType costType,
        CostScope costScope,
        UUID projectId,
        UUID departmentId,
        UUID headquartersId,
        UUID employeeId,
        BigDecimal amount,
        LocalDate occurredDate,
        String description,
        CostSourceType sourceType,
        String sourceReference
    ) {
        public static CostEntryResponse from(final CostEntry costEntry) {
            return new CostEntryResponse(
                costEntry.getId(),
                costEntry.getClosingPeriod().getId(),
                costEntry.getCostCategory().getId(),
                costEntry.getCostType(),
                costEntry.getCostScope(),
                costEntry.getProject() == null ? null : costEntry.getProject().getId(),
                costEntry.getDepartment() == null ? null : costEntry.getDepartment().getId(),
                costEntry.getHeadquarters() == null ? null : costEntry.getHeadquarters().getId(),
                costEntry.getEmployee() == null ? null : costEntry.getEmployee().getId(),
                costEntry.getAmount(),
                costEntry.getOccurredDate(),
                costEntry.getDescription(),
                costEntry.getSourceType(),
                costEntry.getSourceReference()
            );
        }
    }
}

package com.costflow.accounting.interfaces.api.cost;

import com.costflow.accounting.application.cost.CostCategoryApplicationService;
import com.costflow.accounting.application.cost.CreateCostCategoryCommand;
import com.costflow.accounting.application.cost.UpdateCostCategoryCommand;
import com.costflow.accounting.domain.cost.CostCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/api/v1/cost-categories")
public class CostCategoryController {

    private final CostCategoryApplicationService costCategoryApplicationService;

    public CostCategoryController(final CostCategoryApplicationService costCategoryApplicationService) {
        this.costCategoryApplicationService = costCategoryApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CostCategoryResponse create(@Valid @RequestBody final CreateCostCategoryRequest request) {
        final CostCategory costCategory = costCategoryApplicationService.create(new CreateCostCategoryCommand(request.code(), request.name(), request.categoryGroup()));
        return CostCategoryResponse.from(costCategory);
    }

    @GetMapping("/{costCategoryId}")
    public CostCategoryResponse get(@PathVariable final UUID costCategoryId) {
        return CostCategoryResponse.from(costCategoryApplicationService.get(costCategoryId));
    }

    @GetMapping
    public List<CostCategoryResponse> getAll() {
        return costCategoryApplicationService.getAll().stream()
            .map(CostCategoryResponse::from)
            .toList();
    }

    @PutMapping("/{costCategoryId}")
    public CostCategoryResponse update(
        @PathVariable final UUID costCategoryId,
        @Valid @RequestBody final UpdateCostCategoryRequest request
    ) {
        final CostCategory costCategory = costCategoryApplicationService.update(costCategoryId, new UpdateCostCategoryCommand(request.name(), request.categoryGroup()));
        return CostCategoryResponse.from(costCategory);
    }

    @PatchMapping("/{costCategoryId}/active")
    public CostCategoryResponse changeActive(
        @PathVariable final UUID costCategoryId,
        @Valid @RequestBody final ChangeActiveRequest request
    ) {
        final CostCategory costCategory = costCategoryApplicationService.changeActive(costCategoryId, request.active());
        return CostCategoryResponse.from(costCategory);
    }

    public record CreateCostCategoryRequest(
        @NotBlank String code,
        @NotBlank String name,
        @NotBlank String categoryGroup
    ) {
    }

    public record UpdateCostCategoryRequest(
        @NotBlank String name,
        @NotBlank String categoryGroup
    ) {
    }

    public record ChangeActiveRequest(
        boolean active
    ) {
    }

    public record CostCategoryResponse(
        UUID id,
        String code,
        String name,
        String categoryGroup,
        boolean active
    ) {
        public static CostCategoryResponse from(final CostCategory costCategory) {
            return new CostCategoryResponse(
                costCategory.getId(),
                costCategory.getCode(),
                costCategory.getName(),
                costCategory.getCategoryGroup(),
                costCategory.isActive()
            );
        }
    }
}

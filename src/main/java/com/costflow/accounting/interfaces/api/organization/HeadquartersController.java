package com.costflow.accounting.interfaces.api.organization;

import com.costflow.accounting.application.organization.CreateHeadquartersCommand;
import com.costflow.accounting.application.organization.HeadquartersApplicationService;
import com.costflow.accounting.application.organization.UpdateHeadquartersCommand;
import com.costflow.accounting.domain.organization.Headquarters;
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
@RequestMapping("/api/v1/headquarters")
public class HeadquartersController {

    private final HeadquartersApplicationService headquartersApplicationService;

    public HeadquartersController(final HeadquartersApplicationService headquartersApplicationService) {
        this.headquartersApplicationService = headquartersApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HeadquartersResponse create(@Valid @RequestBody final CreateHeadquartersRequest request) {
        final Headquarters headquarters = headquartersApplicationService.create(new CreateHeadquartersCommand(request.code(), request.name()));
        return HeadquartersResponse.from(headquarters);
    }

    @GetMapping("/{headquartersId}")
    public HeadquartersResponse get(@PathVariable final UUID headquartersId) {
        return HeadquartersResponse.from(headquartersApplicationService.get(headquartersId));
    }

    @GetMapping
    public List<HeadquartersResponse> getAll() {
        return headquartersApplicationService.getAll().stream()
            .map(HeadquartersResponse::from)
            .toList();
    }

    @PutMapping("/{headquartersId}")
    public HeadquartersResponse update(
        @PathVariable final UUID headquartersId,
        @Valid @RequestBody final UpdateHeadquartersRequest request
    ) {
        final Headquarters headquarters = headquartersApplicationService.update(headquartersId, new UpdateHeadquartersCommand(request.name()));
        return HeadquartersResponse.from(headquarters);
    }

    @PatchMapping("/{headquartersId}/active")
    public HeadquartersResponse changeActive(
        @PathVariable final UUID headquartersId,
        @Valid @RequestBody final ChangeActiveRequest request
    ) {
        final Headquarters headquarters = headquartersApplicationService.changeActive(headquartersId, request.active());
        return HeadquartersResponse.from(headquarters);
    }

    public record CreateHeadquartersRequest(
        @NotBlank String code,
        @NotBlank String name
    ) {
    }

    public record UpdateHeadquartersRequest(
        @NotBlank String name
    ) {
    }

    public record ChangeActiveRequest(
        boolean active
    ) {
    }

    public record HeadquartersResponse(
        UUID id,
        String code,
        String name,
        boolean active
    ) {
        public static HeadquartersResponse from(final Headquarters headquarters) {
            return new HeadquartersResponse(headquarters.getId(), headquarters.getCode(), headquarters.getName(), headquarters.isActive());
        }
    }
}

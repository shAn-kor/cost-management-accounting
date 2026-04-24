package com.costflow.accounting.interfaces.api.organization;

import com.costflow.accounting.application.organization.CreateDepartmentCommand;
import com.costflow.accounting.application.organization.DepartmentApplicationService;
import com.costflow.accounting.application.organization.UpdateDepartmentCommand;
import com.costflow.accounting.domain.organization.Department;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentApplicationService departmentApplicationService;

    public DepartmentController(final DepartmentApplicationService departmentApplicationService) {
        this.departmentApplicationService = departmentApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse create(@Valid @RequestBody final CreateDepartmentRequest request) {
        final Department department = departmentApplicationService.create(new CreateDepartmentCommand(request.headquartersId(), request.code(), request.name()));
        return DepartmentResponse.from(department);
    }

    @GetMapping("/{departmentId}")
    public DepartmentResponse get(@PathVariable final UUID departmentId) {
        return DepartmentResponse.from(departmentApplicationService.get(departmentId));
    }

    @GetMapping
    public List<DepartmentResponse> getAll() {
        return departmentApplicationService.getAll().stream()
            .map(DepartmentResponse::from)
            .toList();
    }

    @PutMapping("/{departmentId}")
    public DepartmentResponse update(
        @PathVariable final UUID departmentId,
        @Valid @RequestBody final UpdateDepartmentRequest request
    ) {
        final Department department = departmentApplicationService.update(departmentId, new UpdateDepartmentCommand(request.name()));
        return DepartmentResponse.from(department);
    }

    @PatchMapping("/{departmentId}/active")
    public DepartmentResponse changeActive(
        @PathVariable final UUID departmentId,
        @Valid @RequestBody final ChangeActiveRequest request
    ) {
        final Department department = departmentApplicationService.changeActive(departmentId, request.active());
        return DepartmentResponse.from(department);
    }

    public record CreateDepartmentRequest(
        @NotNull UUID headquartersId,
        @NotBlank String code,
        @NotBlank String name
    ) {
    }

    public record UpdateDepartmentRequest(
        @NotBlank String name
    ) {
    }

    public record ChangeActiveRequest(
        boolean active
    ) {
    }

    public record DepartmentResponse(
        UUID id,
        UUID headquartersId,
        String code,
        String name,
        boolean active
    ) {
        public static DepartmentResponse from(final Department department) {
            return new DepartmentResponse(
                department.getId(),
                department.getHeadquarters().getId(),
                department.getCode(),
                department.getName(),
                department.isActive()
            );
        }
    }
}

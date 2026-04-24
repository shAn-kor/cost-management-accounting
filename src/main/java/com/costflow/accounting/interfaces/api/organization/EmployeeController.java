package com.costflow.accounting.interfaces.api.organization;

import com.costflow.accounting.application.organization.CreateEmployeeCommand;
import com.costflow.accounting.application.organization.EmployeeApplicationService;
import com.costflow.accounting.application.organization.UpdateEmployeeCommand;
import com.costflow.accounting.domain.organization.Employee;
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
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeApplicationService employeeApplicationService;

    public EmployeeController(final EmployeeApplicationService employeeApplicationService) {
        this.employeeApplicationService = employeeApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse create(@Valid @RequestBody final CreateEmployeeRequest request) {
        final Employee employee = employeeApplicationService.create(new CreateEmployeeCommand(
            request.departmentId(),
            request.jobGradeId(),
            request.employeeNo(),
            request.name(),
            request.employmentType()
        ));
        return EmployeeResponse.from(employee);
    }

    @GetMapping("/{employeeId}")
    public EmployeeResponse get(@PathVariable final UUID employeeId) {
        return EmployeeResponse.from(employeeApplicationService.get(employeeId));
    }

    @GetMapping
    public List<EmployeeResponse> getAll() {
        return employeeApplicationService.getAll().stream()
            .map(EmployeeResponse::from)
            .toList();
    }

    @PutMapping("/{employeeId}")
    public EmployeeResponse update(
        @PathVariable final UUID employeeId,
        @Valid @RequestBody final UpdateEmployeeRequest request
    ) {
        final Employee employee = employeeApplicationService.update(employeeId, new UpdateEmployeeCommand(
            request.departmentId(),
            request.jobGradeId(),
            request.name(),
            request.employmentType()
        ));
        return EmployeeResponse.from(employee);
    }

    @PatchMapping("/{employeeId}/active")
    public EmployeeResponse changeActive(
        @PathVariable final UUID employeeId,
        @Valid @RequestBody final ChangeActiveRequest request
    ) {
        final Employee employee = employeeApplicationService.changeActive(employeeId, request.active());
        return EmployeeResponse.from(employee);
    }

    public record CreateEmployeeRequest(
        @NotNull UUID departmentId,
        @NotNull UUID jobGradeId,
        @NotBlank String employeeNo,
        @NotBlank String name,
        @NotBlank String employmentType
    ) {
    }

    public record UpdateEmployeeRequest(
        @NotNull UUID departmentId,
        @NotNull UUID jobGradeId,
        @NotBlank String name,
        @NotBlank String employmentType
    ) {
    }

    public record ChangeActiveRequest(
        boolean active
    ) {
    }

    public record EmployeeResponse(
        UUID id,
        UUID departmentId,
        UUID jobGradeId,
        String employeeNo,
        String name,
        String employmentType,
        boolean active
    ) {
        public static EmployeeResponse from(final Employee employee) {
            return new EmployeeResponse(
                employee.getId(),
                employee.getDepartment().getId(),
                employee.getJobGrade().getId(),
                employee.getEmployeeNo(),
                employee.getName(),
                employee.getEmploymentType(),
                employee.isActive()
            );
        }
    }
}

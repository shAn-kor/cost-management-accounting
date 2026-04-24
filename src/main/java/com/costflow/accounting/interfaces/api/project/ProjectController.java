package com.costflow.accounting.interfaces.api.project;

import com.costflow.accounting.application.facade.ProjectRegistrationFacade;
import com.costflow.accounting.application.project.CreateProjectCommand;
import com.costflow.accounting.application.project.ProjectApplicationService;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.project.ProjectStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectRegistrationFacade projectRegistrationFacade;
    private final ProjectApplicationService projectApplicationService;

    public ProjectController(
        final ProjectRegistrationFacade projectRegistrationFacade,
        final ProjectApplicationService projectApplicationService
    ) {
        this.projectRegistrationFacade = projectRegistrationFacade;
        this.projectApplicationService = projectApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(@Valid @RequestBody final CreateProjectRequest request) {
        final Project project = projectRegistrationFacade.create(new CreateProjectCommand(
            request.departmentId(),
            request.code(),
            request.name(),
            request.projectType(),
            request.startDate(),
            request.endDate(),
            request.plannedRevenue(),
            request.plannedCost()
        ));
        return ProjectResponse.from(project);
    }

    @GetMapping("/{projectId}")
    public ProjectResponse get(@PathVariable final UUID projectId) {
        return ProjectResponse.from(projectApplicationService.get(projectId));
    }

    @GetMapping
    public List<ProjectResponse> getAll() {
        return projectApplicationService.getAll().stream()
            .map(ProjectResponse::from)
            .toList();
    }

    public record CreateProjectRequest(
        @NotNull UUID departmentId,
        @NotBlank String code,
        @NotBlank String name,
        @NotBlank String projectType,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @PositiveOrZero BigDecimal plannedRevenue,
        @NotNull @PositiveOrZero BigDecimal plannedCost
    ) {
    }

    public record ProjectResponse(
        UUID id,
        UUID departmentId,
        String code,
        String name,
        String projectType,
        ProjectStatus status,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal plannedRevenue,
        BigDecimal plannedCost
    ) {
        public static ProjectResponse from(final Project project) {
            return new ProjectResponse(
                project.getId(),
                project.getDepartment().getId(),
                project.getCode(),
                project.getName(),
                project.getProjectType(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate(),
                project.getPlannedRevenue(),
                project.getPlannedCost()
            );
        }
    }
}

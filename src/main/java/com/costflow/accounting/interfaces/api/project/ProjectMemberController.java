package com.costflow.accounting.interfaces.api.project;

import com.costflow.accounting.application.project.CreateProjectMemberCommand;
import com.costflow.accounting.application.project.ProjectMemberApplicationService;
import com.costflow.accounting.application.project.UpdateProjectMemberCommand;
import com.costflow.accounting.domain.project.ProjectMember;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@RequestMapping("/api/v1/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberApplicationService projectMemberApplicationService;

    public ProjectMemberController(final ProjectMemberApplicationService projectMemberApplicationService) {
        this.projectMemberApplicationService = projectMemberApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectMemberResponse create(
        @PathVariable final UUID projectId,
        @Valid @RequestBody final CreateProjectMemberRequest request
    ) {
        final ProjectMember projectMember = projectMemberApplicationService.create(new CreateProjectMemberCommand(
            projectId,
            request.employeeId(),
            request.role(),
            request.startDate(),
            request.endDate(),
            request.allocationRate(),
            request.monthlyWorkHours()
        ));
        return ProjectMemberResponse.from(projectMember);
    }

    @GetMapping
    public List<ProjectMemberResponse> getAll(@PathVariable final UUID projectId) {
        return projectMemberApplicationService.getAllByProjectId(projectId).stream()
            .map(ProjectMemberResponse::from)
            .toList();
    }

    @PutMapping("/{projectMemberId}")
    public ProjectMemberResponse update(
        @PathVariable final UUID projectMemberId,
        @Valid @RequestBody final UpdateProjectMemberRequest request
    ) {
        final ProjectMember projectMember = projectMemberApplicationService.update(projectMemberId, new UpdateProjectMemberCommand(
            request.role(),
            request.startDate(),
            request.endDate(),
            request.allocationRate(),
            request.monthlyWorkHours()
        ));
        return ProjectMemberResponse.from(projectMember);
    }

    @PatchMapping("/{projectMemberId}/active")
    public ProjectMemberResponse changeActive(
        @PathVariable final UUID projectMemberId,
        @Valid @RequestBody final ChangeActiveRequest request
    ) {
        final ProjectMember projectMember = projectMemberApplicationService.changeActive(projectMemberId, request.active());
        return ProjectMemberResponse.from(projectMember);
    }

    public record CreateProjectMemberRequest(
        @NotNull UUID employeeId,
        @NotBlank String role,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @Positive BigDecimal allocationRate,
        @NotNull @PositiveOrZero BigDecimal monthlyWorkHours
    ) {
    }

    public record UpdateProjectMemberRequest(
        @NotBlank String role,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @Positive BigDecimal allocationRate,
        @NotNull @PositiveOrZero BigDecimal monthlyWorkHours
    ) {
    }

    public record ChangeActiveRequest(
        boolean active
    ) {
    }

    public record ProjectMemberResponse(
        UUID id,
        UUID projectId,
        UUID employeeId,
        String role,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal allocationRate,
        BigDecimal monthlyWorkHours,
        boolean active
    ) {
        public static ProjectMemberResponse from(final ProjectMember projectMember) {
            return new ProjectMemberResponse(
                projectMember.getId(),
                projectMember.getProject().getId(),
                projectMember.getEmployee().getId(),
                projectMember.getRole(),
                projectMember.getStartDate(),
                projectMember.getEndDate(),
                projectMember.getAllocationRate(),
                projectMember.getMonthlyWorkHours(),
                projectMember.isActive()
            );
        }
    }
}

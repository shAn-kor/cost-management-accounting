package com.costflow.accounting.interfaces.api.organization;

import com.costflow.accounting.application.organization.CreateJobGradeCommand;
import com.costflow.accounting.application.organization.JobGradeApplicationService;
import com.costflow.accounting.application.organization.UpdateJobGradeCommand;
import com.costflow.accounting.domain.organization.JobGrade;
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
@RequestMapping("/api/v1/job-grades")
public class JobGradeController {

    private final JobGradeApplicationService jobGradeApplicationService;

    public JobGradeController(final JobGradeApplicationService jobGradeApplicationService) {
        this.jobGradeApplicationService = jobGradeApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobGradeResponse create(@Valid @RequestBody final CreateJobGradeRequest request) {
        final JobGrade jobGrade = jobGradeApplicationService.create(new CreateJobGradeCommand(request.code(), request.name(), request.sortOrder()));
        return JobGradeResponse.from(jobGrade);
    }

    @GetMapping("/{jobGradeId}")
    public JobGradeResponse get(@PathVariable final UUID jobGradeId) {
        return JobGradeResponse.from(jobGradeApplicationService.get(jobGradeId));
    }

    @GetMapping
    public List<JobGradeResponse> getAll() {
        return jobGradeApplicationService.getAll().stream()
            .map(JobGradeResponse::from)
            .toList();
    }

    @PutMapping("/{jobGradeId}")
    public JobGradeResponse update(
        @PathVariable final UUID jobGradeId,
        @Valid @RequestBody final UpdateJobGradeRequest request
    ) {
        final JobGrade jobGrade = jobGradeApplicationService.update(jobGradeId, new UpdateJobGradeCommand(request.name(), request.sortOrder()));
        return JobGradeResponse.from(jobGrade);
    }

    @PatchMapping("/{jobGradeId}/active")
    public JobGradeResponse changeActive(
        @PathVariable final UUID jobGradeId,
        @Valid @RequestBody final ChangeActiveRequest request
    ) {
        final JobGrade jobGrade = jobGradeApplicationService.changeActive(jobGradeId, request.active());
        return JobGradeResponse.from(jobGrade);
    }

    public record CreateJobGradeRequest(
        @NotBlank String code,
        @NotBlank String name,
        int sortOrder
    ) {
    }

    public record UpdateJobGradeRequest(
        @NotBlank String name,
        int sortOrder
    ) {
    }

    public record ChangeActiveRequest(
        boolean active
    ) {
    }

    public record JobGradeResponse(
        UUID id,
        String code,
        String name,
        int sortOrder,
        boolean active
    ) {
        public static JobGradeResponse from(final JobGrade jobGrade) {
            return new JobGradeResponse(jobGrade.getId(), jobGrade.getCode(), jobGrade.getName(), jobGrade.getSortOrder(), jobGrade.isActive());
        }
    }
}

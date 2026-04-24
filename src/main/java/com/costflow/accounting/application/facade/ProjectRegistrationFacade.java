package com.costflow.accounting.application.facade;

import com.costflow.accounting.application.organization.DepartmentApplicationService;
import com.costflow.accounting.application.project.CreateProjectCommand;
import com.costflow.accounting.application.project.ProjectApplicationService;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.project.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectRegistrationFacade {

    private final DepartmentApplicationService departmentApplicationService;
    private final ProjectApplicationService projectApplicationService;

    public ProjectRegistrationFacade(
        final DepartmentApplicationService departmentApplicationService,
        final ProjectApplicationService projectApplicationService
    ) {
        this.departmentApplicationService = departmentApplicationService;
        this.projectApplicationService = projectApplicationService;
    }

    public Project create(final CreateProjectCommand command) {
        final Department department = departmentApplicationService.get(command.departmentId());
        return projectApplicationService.create(command, department);
    }
}

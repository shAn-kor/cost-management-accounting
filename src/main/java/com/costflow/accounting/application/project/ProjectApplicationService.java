package com.costflow.accounting.application.project;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.project.ProjectRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectApplicationService {

    private final ProjectRepository projectRepository;

    public ProjectApplicationService(final ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Project create(final CreateProjectCommand command, final Department department) {
        final Project project = new Project(
            department,
            command.code(),
            command.name(),
            command.projectType(),
            command.startDate(),
            command.endDate(),
            command.plannedRevenue(),
            command.plannedCost()
        );
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Project get(final UUID projectId) {
        return projectRepository.findById(projectId)
            .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. id=" + projectId));
    }

    @Transactional(readOnly = true)
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Project> getByDepartment(final UUID departmentId) {
        return projectRepository.findAllByDepartmentId(departmentId);
    }

    @Transactional(readOnly = true)
    public List<Project> getByHeadquarters(final UUID headquartersId) {
        return projectRepository.findAllByDepartmentHeadquartersId(headquartersId);
    }
}

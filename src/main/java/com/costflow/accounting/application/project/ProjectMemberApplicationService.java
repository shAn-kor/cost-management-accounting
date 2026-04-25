package com.costflow.accounting.application.project;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.organization.EmployeeRepository;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.project.ProjectMember;
import com.costflow.accounting.domain.project.ProjectMemberRepository;
import com.costflow.accounting.domain.project.ProjectRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectMemberApplicationService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;

    public ProjectMemberApplicationService(
        final ProjectMemberRepository projectMemberRepository,
        final ProjectRepository projectRepository,
        final EmployeeRepository employeeRepository
    ) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public ProjectMember create(final CreateProjectMemberCommand command) {
        final Project project = projectRepository.findById(command.projectId())
            .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. id=" + command.projectId()));
        final Employee employee = employeeRepository.findById(command.employeeId())
            .orElseThrow(() -> new NotFoundException("인력을 찾을 수 없습니다. id=" + command.employeeId()));
        final ProjectMember projectMember = new ProjectMember(
            project,
            employee,
            command.role(),
            command.startDate(),
            command.endDate(),
            command.allocationRate(),
            command.monthlyWorkHours()
        );
        return projectMemberRepository.save(projectMember);
    }

    @Transactional
    public ProjectMember update(final UUID projectMemberId, final UpdateProjectMemberCommand command) {
        final ProjectMember projectMember = get(projectMemberId);
        projectMember.update(
            command.role(),
            command.startDate(),
            command.endDate(),
            command.allocationRate(),
            command.monthlyWorkHours()
        );
        return projectMember;
    }

    @Transactional
    public ProjectMember changeActive(final UUID projectMemberId, final boolean active) {
        final ProjectMember projectMember = get(projectMemberId);
        projectMember.changeActive(active);
        return projectMember;
    }

    @Transactional(readOnly = true)
    public ProjectMember get(final UUID projectMemberId) {
        return projectMemberRepository.findById(projectMemberId)
            .orElseThrow(() -> new NotFoundException("프로젝트 참여 인력을 찾을 수 없습니다. id=" + projectMemberId));
    }

    @Transactional(readOnly = true)
    public List<ProjectMember> getAllByProjectId(final UUID projectId) {
        return projectMemberRepository.findAllByProjectId(projectId);
    }

    @Transactional(readOnly = true)
    public List<ProjectMember> getAllActive() {
        return projectMemberRepository.findAllByActiveTrue();
    }
}

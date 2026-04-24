package com.costflow.accounting.domain.project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepository {

    ProjectMember save(ProjectMember projectMember);

    Optional<ProjectMember> findById(UUID id);

    List<ProjectMember> findAllByProjectId(UUID projectId);
}

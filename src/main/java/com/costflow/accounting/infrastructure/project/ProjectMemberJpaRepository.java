package com.costflow.accounting.infrastructure.project;

import com.costflow.accounting.domain.project.ProjectMember;
import com.costflow.accounting.domain.project.ProjectMemberRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberJpaRepository extends JpaRepository<ProjectMember, UUID>, ProjectMemberRepository {

    @Override
    List<ProjectMember> findAllByProjectId(UUID projectId);
}

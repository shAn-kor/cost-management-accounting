package com.costflow.accounting.infrastructure.project;

import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.project.ProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProjectJpaRepository extends JpaRepository<Project, UUID>, ProjectRepository {
}

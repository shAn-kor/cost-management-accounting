package com.costflow.accounting.domain.project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {

    Project save(Project project);

    Optional<Project> findById(UUID id);

    List<Project> findAll();

    List<Project> findAllByDepartmentId(UUID departmentId);

    List<Project> findAllByDepartmentHeadquartersId(UUID headquartersId);
}

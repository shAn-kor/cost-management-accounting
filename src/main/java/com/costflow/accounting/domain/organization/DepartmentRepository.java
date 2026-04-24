package com.costflow.accounting.domain.organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {

    Department save(Department department);

    Optional<Department> findById(UUID id);

    List<Department> findAll();
}

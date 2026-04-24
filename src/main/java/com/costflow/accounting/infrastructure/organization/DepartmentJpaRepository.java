package com.costflow.accounting.infrastructure.organization;

import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.DepartmentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DepartmentJpaRepository extends JpaRepository<Department, UUID>, DepartmentRepository {
}

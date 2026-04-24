package com.costflow.accounting.infrastructure.organization;

import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.organization.EmployeeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EmployeeJpaRepository extends JpaRepository<Employee, UUID>, EmployeeRepository {
}

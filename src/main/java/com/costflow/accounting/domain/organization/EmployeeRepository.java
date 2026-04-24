package com.costflow.accounting.domain.organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Optional<Employee> findById(UUID id);

    List<Employee> findAll();
}

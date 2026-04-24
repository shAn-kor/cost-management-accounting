package com.costflow.accounting.application.organization;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.DepartmentRepository;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.organization.EmployeeRepository;
import com.costflow.accounting.domain.organization.JobGrade;
import com.costflow.accounting.domain.organization.JobGradeRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeApplicationService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final JobGradeRepository jobGradeRepository;

    public EmployeeApplicationService(
        final EmployeeRepository employeeRepository,
        final DepartmentRepository departmentRepository,
        final JobGradeRepository jobGradeRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.jobGradeRepository = jobGradeRepository;
    }

    @Transactional
    public Employee create(final CreateEmployeeCommand command) {
        final Department department = departmentRepository.findById(command.departmentId())
            .orElseThrow(() -> new NotFoundException("부서를 찾을 수 없습니다. id=" + command.departmentId()));
        final JobGrade jobGrade = jobGradeRepository.findById(command.jobGradeId())
            .orElseThrow(() -> new NotFoundException("직급을 찾을 수 없습니다. id=" + command.jobGradeId()));
        return employeeRepository.save(new Employee(department, jobGrade, command.employeeNo(), command.name(), command.employmentType()));
    }

    @Transactional
    public Employee update(final UUID employeeId, final UpdateEmployeeCommand command) {
        final Employee employee = get(employeeId);
        final Department department = departmentRepository.findById(command.departmentId())
            .orElseThrow(() -> new NotFoundException("부서를 찾을 수 없습니다. id=" + command.departmentId()));
        final JobGrade jobGrade = jobGradeRepository.findById(command.jobGradeId())
            .orElseThrow(() -> new NotFoundException("직급을 찾을 수 없습니다. id=" + command.jobGradeId()));
        employee.update(department, jobGrade, command.name(), command.employmentType());
        return employee;
    }

    @Transactional
    public Employee changeActive(final UUID employeeId, final boolean active) {
        final Employee employee = get(employeeId);
        employee.changeActive(active);
        return employee;
    }

    @Transactional(readOnly = true)
    public Employee get(final UUID employeeId) {
        return employeeRepository.findById(employeeId)
            .orElseThrow(() -> new NotFoundException("인력을 찾을 수 없습니다. id=" + employeeId));
    }

    @Transactional(readOnly = true)
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }
}

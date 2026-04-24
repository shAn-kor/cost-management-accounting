package com.costflow.accounting.application.organization;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.DepartmentRepository;
import com.costflow.accounting.domain.organization.Headquarters;
import com.costflow.accounting.domain.organization.HeadquartersRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentApplicationService {

    private final DepartmentRepository departmentRepository;
    private final HeadquartersRepository headquartersRepository;

    public DepartmentApplicationService(final DepartmentRepository departmentRepository, final HeadquartersRepository headquartersRepository) {
        this.departmentRepository = departmentRepository;
        this.headquartersRepository = headquartersRepository;
    }

    @Transactional
    public Department create(final CreateDepartmentCommand command) {
        final Headquarters headquarters = headquartersRepository.findById(command.headquartersId())
            .orElseThrow(() -> new NotFoundException("본부를 찾을 수 없습니다. id=" + command.headquartersId()));
        return departmentRepository.save(new Department(headquarters, command.code(), command.name()));
    }

    @Transactional
    public Department update(final UUID departmentId, final UpdateDepartmentCommand command) {
        final Department department = get(departmentId);
        department.update(command.name());
        return department;
    }

    @Transactional
    public Department changeActive(final UUID departmentId, final boolean active) {
        final Department department = get(departmentId);
        department.changeActive(active);
        return department;
    }

    @Transactional(readOnly = true)
    public Department get(final UUID departmentId) {
        return departmentRepository.findById(departmentId)
            .orElseThrow(() -> new NotFoundException("부서를 찾을 수 없습니다. id=" + departmentId));
    }

    @Transactional(readOnly = true)
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }
}

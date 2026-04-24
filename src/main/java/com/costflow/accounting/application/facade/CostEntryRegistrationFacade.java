package com.costflow.accounting.application.facade;

import com.costflow.accounting.application.closing.ClosingApplicationService;
import com.costflow.accounting.application.cost.CostCategoryApplicationService;
import com.costflow.accounting.application.cost.CostEntryApplicationService;
import com.costflow.accounting.application.cost.CreateCostEntryCommand;
import com.costflow.accounting.application.cost.UpdateCostEntryCommand;
import com.costflow.accounting.application.organization.DepartmentApplicationService;
import com.costflow.accounting.application.organization.EmployeeApplicationService;
import com.costflow.accounting.application.organization.HeadquartersApplicationService;
import com.costflow.accounting.application.project.ProjectApplicationService;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.cost.CostCategory;
import com.costflow.accounting.domain.cost.CostEntry;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.organization.Headquarters;
import com.costflow.accounting.domain.project.Project;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CostEntryRegistrationFacade {

    private final ClosingApplicationService closingApplicationService;
    private final CostCategoryApplicationService costCategoryApplicationService;
    private final CostEntryApplicationService costEntryApplicationService;
    private final ProjectApplicationService projectApplicationService;
    private final DepartmentApplicationService departmentApplicationService;
    private final HeadquartersApplicationService headquartersApplicationService;
    private final EmployeeApplicationService employeeApplicationService;

    public CostEntryRegistrationFacade(
        final ClosingApplicationService closingApplicationService,
        final CostCategoryApplicationService costCategoryApplicationService,
        final CostEntryApplicationService costEntryApplicationService,
        final ProjectApplicationService projectApplicationService,
        final DepartmentApplicationService departmentApplicationService,
        final HeadquartersApplicationService headquartersApplicationService,
        final EmployeeApplicationService employeeApplicationService
    ) {
        this.closingApplicationService = closingApplicationService;
        this.costCategoryApplicationService = costCategoryApplicationService;
        this.costEntryApplicationService = costEntryApplicationService;
        this.projectApplicationService = projectApplicationService;
        this.departmentApplicationService = departmentApplicationService;
        this.headquartersApplicationService = headquartersApplicationService;
        this.employeeApplicationService = employeeApplicationService;
    }

    public CostEntry create(final CreateCostEntryCommand command) {
        closingApplicationService.validateCostEditable(command.closingPeriodId());
        final ClosingPeriod closingPeriod = closingApplicationService.get(command.closingPeriodId());
        final CostCategory costCategory = costCategoryApplicationService.get(command.costCategoryId());
        final Project project = resolveProject(command);
        final Department department = resolveDepartment(command);
        final Headquarters headquarters = resolveHeadquarters(command);
        final Employee employee = resolveEmployee(command);
        return costEntryApplicationService.create(command, closingPeriod, costCategory, project, department, headquarters, employee);
    }

    public CostEntry update(final UpdateCostEntryCommand command) {
        final CostEntry costEntry = costEntryApplicationService.get(command.costEntryId());
        closingApplicationService.validateCostEditable(costEntry.getClosingPeriod().getId());
        final CostCategory costCategory = costCategoryApplicationService.get(command.costCategoryId());
        final Project project = resolveProject(command.projectId());
        final Department department = resolveDepartment(command.departmentId());
        final Headquarters headquarters = resolveHeadquarters(command.headquartersId());
        final Employee employee = resolveEmployee(command.employeeId());
        return costEntryApplicationService.update(command, costCategory, project, department, headquarters, employee);
    }

    public void delete(final UUID costEntryId) {
        final CostEntry costEntry = costEntryApplicationService.get(costEntryId);
        closingApplicationService.validateCostEditable(costEntry.getClosingPeriod().getId());
        costEntryApplicationService.delete(costEntry);
    }

    public CostEntry get(final UUID costEntryId) {
        return costEntryApplicationService.get(costEntryId);
    }

    public List<CostEntry> getByClosingPeriod(final UUID closingPeriodId) {
        return costEntryApplicationService.getByClosingPeriod(closingPeriodId);
    }

    public Project resolveProject(final CreateCostEntryCommand command) {
        return resolveProject(command.projectId());
    }

    public Project resolveProject(final UUID projectId) {
        if (projectId != null) {
            return projectApplicationService.get(projectId);
        }
        return null;
    }

    public Department resolveDepartment(final CreateCostEntryCommand command) {
        return resolveDepartment(command.departmentId());
    }

    public Department resolveDepartment(final UUID departmentId) {
        if (departmentId != null) {
            return departmentApplicationService.get(departmentId);
        }
        return null;
    }

    public Headquarters resolveHeadquarters(final CreateCostEntryCommand command) {
        return resolveHeadquarters(command.headquartersId());
    }

    public Headquarters resolveHeadquarters(final UUID headquartersId) {
        if (headquartersId != null) {
            return headquartersApplicationService.get(headquartersId);
        }
        return null;
    }

    public Employee resolveEmployee(final CreateCostEntryCommand command) {
        return resolveEmployee(command.employeeId());
    }

    public Employee resolveEmployee(final UUID employeeId) {
        if (employeeId != null) {
            return employeeApplicationService.get(employeeId);
        }
        return null;
    }
}

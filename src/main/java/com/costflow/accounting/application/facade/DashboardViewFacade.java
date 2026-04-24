package com.costflow.accounting.application.facade;

import com.costflow.accounting.application.closing.ClosingApplicationService;
import com.costflow.accounting.application.cost.CostCategoryApplicationService;
import com.costflow.accounting.application.organization.DepartmentApplicationService;
import com.costflow.accounting.application.organization.EmployeeApplicationService;
import com.costflow.accounting.application.organization.HeadquartersApplicationService;
import com.costflow.accounting.application.project.ProjectApplicationService;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.project.Project;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class DashboardViewFacade {

    private final HeadquartersApplicationService headquartersApplicationService;
    private final DepartmentApplicationService departmentApplicationService;
    private final EmployeeApplicationService employeeApplicationService;
    private final ProjectApplicationService projectApplicationService;
    private final CostCategoryApplicationService costCategoryApplicationService;
    private final ClosingApplicationService closingApplicationService;

    public DashboardViewFacade(
        final HeadquartersApplicationService headquartersApplicationService,
        final DepartmentApplicationService departmentApplicationService,
        final EmployeeApplicationService employeeApplicationService,
        final ProjectApplicationService projectApplicationService,
        final CostCategoryApplicationService costCategoryApplicationService,
        final ClosingApplicationService closingApplicationService
    ) {
        this.headquartersApplicationService = headquartersApplicationService;
        this.departmentApplicationService = departmentApplicationService;
        this.employeeApplicationService = employeeApplicationService;
        this.projectApplicationService = projectApplicationService;
        this.costCategoryApplicationService = costCategoryApplicationService;
        this.closingApplicationService = closingApplicationService;
    }

    public DashboardViewData getDashboardViewData() {
        final List<ClosingPeriod> closingPeriods = closingApplicationService.getAll();
        final List<Project> projects = projectApplicationService.getAll();
        return new DashboardViewData(
            headquartersApplicationService.getAll().size(),
            departmentApplicationService.getAll().size(),
            employeeApplicationService.getAll().size(),
            projects.size(),
            costCategoryApplicationService.getAll().size(),
            closingPeriods,
            projects
        );
    }

    public record DashboardViewData(
        int headquartersCount,
        int departmentCount,
        int employeeCount,
        int projectCount,
        int costCategoryCount,
        List<ClosingPeriod> closingPeriods,
        List<Project> projects
    ) {
    }
}

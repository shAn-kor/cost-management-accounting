package com.costflow.accounting.application.facade;

import com.costflow.accounting.application.allocation.AllocationApplicationService;
import com.costflow.accounting.application.allocation.CreateAllocationRuleCommand;
import com.costflow.accounting.application.allocation.ExecuteAllocationCommand;
import com.costflow.accounting.application.closing.ClosingApplicationService;
import com.costflow.accounting.application.closing.CreateClosingPeriodCommand;
import com.costflow.accounting.application.cost.CostCategoryApplicationService;
import com.costflow.accounting.application.cost.CreateCostCategoryCommand;
import com.costflow.accounting.application.cost.CreateCostEntryCommand;
import com.costflow.accounting.application.organization.CreateDepartmentCommand;
import com.costflow.accounting.application.organization.CreateEmployeeCommand;
import com.costflow.accounting.application.organization.CreateHeadquartersCommand;
import com.costflow.accounting.application.organization.CreateJobGradeCommand;
import com.costflow.accounting.application.organization.DepartmentApplicationService;
import com.costflow.accounting.application.organization.EmployeeApplicationService;
import com.costflow.accounting.application.organization.HeadquartersApplicationService;
import com.costflow.accounting.application.organization.JobGradeApplicationService;
import com.costflow.accounting.application.project.CreateProjectCommand;
import com.costflow.accounting.application.project.CreateProjectMemberCommand;
import com.costflow.accounting.application.project.ProjectApplicationService;
import com.costflow.accounting.application.project.ProjectMemberApplicationService;
import com.costflow.accounting.application.standardcost.CreateStandardLaborRateCommand;
import com.costflow.accounting.application.standardcost.StandardCostResultApplicationService;
import com.costflow.accounting.application.standardcost.StandardLaborRateApplicationService;
import com.costflow.accounting.application.transferprice.TransferPriceApplicationService;
import com.costflow.accounting.domain.allocation.AllocationExecution;
import com.costflow.accounting.domain.allocation.AllocationExecutionStatus;
import com.costflow.accounting.domain.allocation.AllocationRule;
import com.costflow.accounting.domain.allocation.AllocationType;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.closing.ClosingStatus;
import com.costflow.accounting.domain.cost.CostScope;
import com.costflow.accounting.domain.cost.CostSourceType;
import com.costflow.accounting.domain.cost.CostType;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.organization.Headquarters;
import com.costflow.accounting.domain.organization.JobGrade;
import com.costflow.accounting.domain.project.Project;
import com.costflow.accounting.domain.project.ProjectMember;
import com.costflow.accounting.domain.standardcost.StandardLaborRate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OperationsWorkspaceFacade {

    private final HeadquartersApplicationService headquartersApplicationService;
    private final DepartmentApplicationService departmentApplicationService;
    private final JobGradeApplicationService jobGradeApplicationService;
    private final EmployeeApplicationService employeeApplicationService;
    private final CostCategoryApplicationService costCategoryApplicationService;
    private final ProjectRegistrationFacade projectRegistrationFacade;
    private final ProjectApplicationService projectApplicationService;
    private final ProjectMemberApplicationService projectMemberApplicationService;
    private final ClosingApplicationService closingApplicationService;
    private final CostEntryRegistrationFacade costEntryRegistrationFacade;
    private final StandardLaborRateApplicationService standardLaborRateApplicationService;
    private final StandardCostResultApplicationService standardCostResultApplicationService;
    private final StandardCostCalculationFacade standardCostCalculationFacade;
    private final AllocationApplicationService allocationApplicationService;
    private final AllocationExecutionFacade allocationExecutionFacade;
    private final TransferPriceApplicationService transferPriceApplicationService;
    private final TransferPriceCalculationFacade transferPriceCalculationFacade;

    public OperationsWorkspaceFacade(
        final HeadquartersApplicationService headquartersApplicationService,
        final DepartmentApplicationService departmentApplicationService,
        final JobGradeApplicationService jobGradeApplicationService,
        final EmployeeApplicationService employeeApplicationService,
        final CostCategoryApplicationService costCategoryApplicationService,
        final ProjectRegistrationFacade projectRegistrationFacade,
        final ProjectApplicationService projectApplicationService,
        final ProjectMemberApplicationService projectMemberApplicationService,
        final ClosingApplicationService closingApplicationService,
        final CostEntryRegistrationFacade costEntryRegistrationFacade,
        final StandardLaborRateApplicationService standardLaborRateApplicationService,
        final StandardCostResultApplicationService standardCostResultApplicationService,
        final StandardCostCalculationFacade standardCostCalculationFacade,
        final AllocationApplicationService allocationApplicationService,
        final AllocationExecutionFacade allocationExecutionFacade,
        final TransferPriceApplicationService transferPriceApplicationService,
        final TransferPriceCalculationFacade transferPriceCalculationFacade
    ) {
        this.headquartersApplicationService = headquartersApplicationService;
        this.departmentApplicationService = departmentApplicationService;
        this.jobGradeApplicationService = jobGradeApplicationService;
        this.employeeApplicationService = employeeApplicationService;
        this.costCategoryApplicationService = costCategoryApplicationService;
        this.projectRegistrationFacade = projectRegistrationFacade;
        this.projectApplicationService = projectApplicationService;
        this.projectMemberApplicationService = projectMemberApplicationService;
        this.closingApplicationService = closingApplicationService;
        this.costEntryRegistrationFacade = costEntryRegistrationFacade;
        this.standardLaborRateApplicationService = standardLaborRateApplicationService;
        this.standardCostResultApplicationService = standardCostResultApplicationService;
        this.standardCostCalculationFacade = standardCostCalculationFacade;
        this.allocationApplicationService = allocationApplicationService;
        this.allocationExecutionFacade = allocationExecutionFacade;
        this.transferPriceApplicationService = transferPriceApplicationService;
        this.transferPriceCalculationFacade = transferPriceCalculationFacade;
    }

    @Transactional(readOnly = true)
    public OperationsWorkspaceData getWorkspaceData(final UUID focusedClosingPeriodId) {
        final List<Headquarters> headquarters = headquartersApplicationService.getAll().stream()
            .sorted(Comparator.comparing(Headquarters::getCode))
            .toList();
        final List<Department> departments = departmentApplicationService.getAll().stream()
            .sorted(Comparator.comparing((Department department) -> department.getHeadquarters().getCode())
                .thenComparing(Department::getCode))
            .toList();
        final List<JobGrade> jobGrades = jobGradeApplicationService.getAll().stream()
            .sorted(Comparator.comparingInt(JobGrade::getSortOrder).thenComparing(JobGrade::getCode))
            .toList();
        final List<Employee> employees = employeeApplicationService.getAll().stream()
            .sorted(Comparator.comparing(Employee::getEmployeeNo))
            .toList();
        final List<Project> projects = projectApplicationService.getAll().stream()
            .sorted(Comparator.comparing(Project::getCode))
            .toList();
        final List<ProjectMember> projectMembers = projectMemberApplicationService.getAllActive().stream()
            .sorted(Comparator.comparing((ProjectMember projectMember) -> projectMember.getProject().getCode())
                .thenComparing(projectMember -> projectMember.getEmployee().getEmployeeNo()))
            .toList();
        final List<ClosingPeriod> closingPeriods = closingApplicationService.getAll().stream()
            .sorted(Comparator.comparing(ClosingPeriod::getYearMonth).reversed())
            .toList();
        final ClosingPeriod focusedClosingPeriod = closingPeriods.stream()
            .filter(closingPeriod -> closingPeriod.getId().equals(focusedClosingPeriodId))
            .findFirst()
            .orElse(closingPeriods.isEmpty() ? null : closingPeriods.get(0));
        final UUID resolvedFocusedClosingPeriodId = focusedClosingPeriod == null ? null : focusedClosingPeriod.getId();
        final List<StandardLaborRate> standardLaborRates = standardLaborRateApplicationService.getAll().stream()
            .sorted(Comparator.comparing((StandardLaborRate rate) -> rate.getJobGrade().getSortOrder())
                .thenComparing(StandardLaborRate::getEffectiveFrom).reversed())
            .toList();
        final List<AllocationRule> allocationRules = allocationApplicationService.getRules().stream()
            .sorted(Comparator.comparing(AllocationRule::getName))
            .toList();

        final List<CostEntryItem> costEntries = focusedClosingPeriod == null
            ? List.of()
            : costEntryRegistrationFacade.getByClosingPeriod(focusedClosingPeriod.getId()).stream()
                .sorted(Comparator.comparing(com.costflow.accounting.domain.cost.CostEntry::getOccurredDate)
                    .reversed()
                    .thenComparing(com.costflow.accounting.domain.cost.CostEntry::getDescription))
                .map(costEntry -> new CostEntryItem(
                    costEntry.getId(),
                    costEntry.getOccurredDate(),
                    costEntry.getCostCategory().getCode(),
                    costEntry.getCostCategory().getName(),
                    costEntry.getCostType(),
                    costEntry.getCostScope(),
                    costEntry.getAmount(),
                    costEntry.getDescription(),
                    costEntry.getProject() == null ? null : costEntry.getProject().getCode(),
                    costEntry.getProject() == null ? null : costEntry.getProject().getName(),
                    costEntry.getDepartment() == null ? null : costEntry.getDepartment().getName(),
                    costEntry.getHeadquarters() == null ? null : costEntry.getHeadquarters().getName(),
                    costEntry.getEmployee() == null ? null : costEntry.getEmployee().getName(),
                    costEntry.getSourceType(),
                    costEntry.getSourceReference()
                ))
                .toList();
        final List<StandardCostResultItem> standardCostResults = focusedClosingPeriod == null
            ? List.of()
            : standardCostResultApplicationService.getByClosingPeriod(focusedClosingPeriod.getId()).stream()
                .sorted(Comparator.comparing((com.costflow.accounting.domain.standardcost.StandardCostResult result) -> result.getCalculationVersion()).reversed()
                    .thenComparing(result -> result.getProject().getCode()))
                .map(result -> new StandardCostResultItem(
                    result.getId(),
                    result.getProject().getCode(),
                    result.getProject().getName(),
                    result.getStandardLaborCost(),
                    result.getStandardCommonCost(),
                    result.getTotalStandardCost(),
                    result.getCalculationVersion()
                ))
                .toList();
        final List<AllocationExecutionItem> allocationExecutions = focusedClosingPeriod == null
            ? List.of()
            : allocationApplicationService.getExecutionsByClosingPeriod(focusedClosingPeriod.getId()).stream()
                .sorted(Comparator.comparing(AllocationExecution::getExecutionVersion).reversed())
                .map(execution -> new AllocationExecutionItem(
                    execution.getId(),
                    execution.getAllocationRule().getName(),
                    execution.getExecutionVersion(),
                    execution.getStatus(),
                    execution.getFailureReason(),
                    execution.getIdempotencyKey()
                ))
                .toList();
        final List<AllocationResultItem> allocationResults = focusedClosingPeriod == null
            ? List.of()
            : allocationApplicationService.getResultsByClosingPeriod(focusedClosingPeriod.getId()).stream()
                .sorted(Comparator.comparing((com.costflow.accounting.domain.allocation.AllocationResult result) -> result.getAllocationExecution().getExecutionVersion()).reversed()
                    .thenComparing(result -> result.getTargetProject().getCode()))
                .map(result -> new AllocationResultItem(
                    result.getId(),
                    result.getAllocationExecution().getExecutionVersion(),
                    result.getAllocationExecution().getStatus(),
                    result.getAllocationExecution().getAllocationRule().getName(),
                    result.getSourceCostEntry().getDescription(),
                    result.getTargetProject().getCode(),
                    result.getTargetProject().getName(),
                    result.getBasisValue(),
                    result.getAllocationRate(),
                    result.getAllocationRate().multiply(BigDecimal.valueOf(100L)),
                    result.getAllocatedAmount(),
                    result.getAdjustmentAmount()
                ))
                .toList();
        final List<TransferPriceResultItem> transferPriceResults = focusedClosingPeriod == null
            ? List.of()
            : transferPriceApplicationService.getByClosingPeriod(focusedClosingPeriod.getId()).stream()
                .sorted(Comparator.comparing((com.costflow.accounting.domain.transferprice.TransferPriceResult result) -> result.getCalculationVersion()).reversed()
                    .thenComparing(result -> result.getProject().getCode())
                    .thenComparing(result -> result.getEmployee().getEmployeeNo()))
                .map(result -> new TransferPriceResultItem(
                    result.getId(),
                    result.getProject().getCode(),
                    result.getProject().getName(),
                    result.getEmployee().getEmployeeNo(),
                    result.getEmployee().getName(),
                    result.getDepartment().getName(),
                    result.getStandardLaborRate().getJobGrade().getName(),
                    result.getWorkHours(),
                    result.getHourlyRate(),
                    result.getTransferAmount(),
                    result.getCalculationVersion()
                ))
                .toList();

        return new OperationsWorkspaceData(
            headquarters.stream()
                .map(item -> new HeadquartersItem(item.getId(), item.getCode(), item.getName(), item.isActive()))
                .toList(),
            departments.stream()
                .map(item -> new DepartmentItem(
                    item.getId(),
                    item.getHeadquarters().getId(),
                    item.getHeadquarters().getCode(),
                    item.getHeadquarters().getName(),
                    item.getCode(),
                    item.getName(),
                    item.isActive()
                ))
                .toList(),
            jobGrades.stream()
                .map(item -> new JobGradeItem(item.getId(), item.getCode(), item.getName(), item.getSortOrder(), item.isActive()))
                .toList(),
            employees.stream()
                .map(item -> new EmployeeItem(
                    item.getId(),
                    item.getDepartment().getId(),
                    item.getDepartment().getName(),
                    item.getJobGrade().getId(),
                    item.getJobGrade().getName(),
                    item.getEmployeeNo(),
                    item.getName(),
                    item.getEmploymentType(),
                    item.isActive()
                ))
                .toList(),
            costCategoryApplicationService.getAll().stream()
                .sorted(Comparator.comparing(com.costflow.accounting.domain.cost.CostCategory::getCode))
                .map(item -> new CostCategoryItem(item.getId(), item.getCode(), item.getName(), item.getCategoryGroup(), item.isActive()))
                .toList(),
            projects.stream()
                .map(item -> new ProjectItem(
                    item.getId(),
                    item.getDepartment().getId(),
                    item.getDepartment().getCode(),
                    item.getDepartment().getName(),
                    item.getCode(),
                    item.getName(),
                    item.getProjectType(),
                    item.getStatus().name(),
                    item.getStartDate(),
                    item.getEndDate(),
                    item.getPlannedRevenue(),
                    item.getPlannedCost()
                ))
                .toList(),
            projectMembers.stream()
                .map(item -> new ProjectMemberItem(
                    item.getId(),
                    item.getProject().getId(),
                    item.getProject().getCode(),
                    item.getProject().getName(),
                    item.getEmployee().getId(),
                    item.getEmployee().getEmployeeNo(),
                    item.getEmployee().getName(),
                    item.getEmployee().getDepartment().getName(),
                    item.getEmployee().getJobGrade().getName(),
                    item.getRole(),
                    item.getStartDate(),
                    item.getEndDate(),
                    item.getAllocationRate(),
                    item.getMonthlyWorkHours(),
                    item.isActive()
                ))
                .toList(),
            closingPeriods.stream()
                .map(item -> new ClosingPeriodItem(item.getId(), item.getYearMonth(), item.getStatus(), item.getVersion()))
                .toList(),
            resolvedFocusedClosingPeriodId,
            focusedClosingPeriod == null ? null : focusedClosingPeriod.getYearMonth(),
            costEntries,
            standardLaborRates.stream()
                .map(item -> new StandardLaborRateItem(
                    item.getId(),
                    item.getJobGrade().getId(),
                    item.getJobGrade().getCode(),
                    item.getJobGrade().getName(),
                    item.getHourlyRate(),
                    item.getEffectiveFrom(),
                    item.getEffectiveTo(),
                    item.isActive()
                ))
                .toList(),
            standardCostResults,
            allocationRules.stream()
                .map(item -> new AllocationRuleItem(
                    item.getId(),
                    item.getName(),
                    item.getAllocationType(),
                    item.getSourceScope(),
                    item.getTargetScope(),
                    item.isActive()
                ))
                .toList(),
            allocationExecutions,
            allocationResults,
            transferPriceResults
        );
    }

    public Headquarters createHeadquarters(final String code, final String name) {
        return headquartersApplicationService.create(new CreateHeadquartersCommand(code, name));
    }

    public Department createDepartment(final UUID headquartersId, final String code, final String name) {
        return departmentApplicationService.create(new CreateDepartmentCommand(headquartersId, code, name));
    }

    public JobGrade createJobGrade(final String code, final String name, final int sortOrder) {
        return jobGradeApplicationService.create(new CreateJobGradeCommand(code, name, sortOrder));
    }

    public Employee createEmployee(
        final UUID departmentId,
        final UUID jobGradeId,
        final String employeeNo,
        final String name,
        final String employmentType
    ) {
        return employeeApplicationService.create(new CreateEmployeeCommand(departmentId, jobGradeId, employeeNo, name, employmentType));
    }

    public com.costflow.accounting.domain.cost.CostCategory createCostCategory(
        final String code,
        final String name,
        final String categoryGroup
    ) {
        return costCategoryApplicationService.create(new CreateCostCategoryCommand(code, name, categoryGroup));
    }

    public Project createProject(
        final UUID departmentId,
        final String code,
        final String name,
        final String projectType,
        final LocalDate startDate,
        final LocalDate endDate,
        final BigDecimal plannedRevenue,
        final BigDecimal plannedCost
    ) {
        return projectRegistrationFacade.create(new CreateProjectCommand(
            departmentId,
            code,
            name,
            projectType,
            startDate,
            endDate,
            plannedRevenue,
            plannedCost
        ));
    }

    public ProjectMember createProjectMember(
        final UUID projectId,
        final UUID employeeId,
        final String role,
        final LocalDate startDate,
        final LocalDate endDate,
        final BigDecimal allocationRate,
        final BigDecimal monthlyWorkHours
    ) {
        return projectMemberApplicationService.create(new CreateProjectMemberCommand(
            projectId,
            employeeId,
            role,
            startDate,
            endDate,
            allocationRate,
            monthlyWorkHours
        ));
    }

    public ClosingPeriod createClosingPeriod(final String yearMonth) {
        return closingApplicationService.create(new CreateClosingPeriodCommand(yearMonth));
    }

    public ClosingPeriod transitionClosingPeriod(final UUID closingPeriodId, final ClosingStatus nextStatus) {
        return closingApplicationService.transition(closingPeriodId, nextStatus);
    }

    public com.costflow.accounting.domain.cost.CostEntry createCostEntry(
        final UUID closingPeriodId,
        final UUID costCategoryId,
        final CostType costType,
        final CostScope costScope,
        final UUID projectId,
        final UUID departmentId,
        final UUID headquartersId,
        final UUID employeeId,
        final BigDecimal amount,
        final LocalDate occurredDate,
        final String description,
        final CostSourceType sourceType,
        final String sourceReference
    ) {
        return costEntryRegistrationFacade.create(new CreateCostEntryCommand(
            closingPeriodId,
            costCategoryId,
            costType,
            costScope,
            projectId,
            departmentId,
            headquartersId,
            employeeId,
            amount,
            occurredDate,
            description,
            sourceType,
            sourceReference
        ));
    }

    public StandardLaborRate createStandardLaborRate(
        final UUID jobGradeId,
        final BigDecimal hourlyRate,
        final LocalDate effectiveFrom,
        final LocalDate effectiveTo
    ) {
        return standardLaborRateApplicationService.create(new CreateStandardLaborRateCommand(jobGradeId, hourlyRate, effectiveFrom, effectiveTo));
    }

    public List<com.costflow.accounting.domain.standardcost.StandardCostResult> calculateStandardCosts(final UUID closingPeriodId) {
        return standardCostCalculationFacade.calculate(closingPeriodId);
    }

    public AllocationRule createAllocationRule(final String name, final AllocationType allocationType, final CostScope sourceScope) {
        return allocationApplicationService.createRule(new CreateAllocationRuleCommand(name, allocationType, sourceScope, CostScope.PROJECT));
    }

    public AllocationExecution executeAllocation(final UUID closingPeriodId, final UUID allocationRuleId, final String idempotencyKey) {
        final String resolvedIdempotencyKey = idempotencyKey == null || idempotencyKey.isBlank()
            ? UUID.randomUUID().toString()
            : idempotencyKey;
        return allocationExecutionFacade.execute(new ExecuteAllocationCommand(closingPeriodId, allocationRuleId, resolvedIdempotencyKey));
    }

    public List<com.costflow.accounting.domain.transferprice.TransferPriceResult> calculateTransferPrices(final UUID closingPeriodId) {
        return transferPriceCalculationFacade.calculate(closingPeriodId);
    }

    public record OperationsWorkspaceData(
        List<HeadquartersItem> headquarters,
        List<DepartmentItem> departments,
        List<JobGradeItem> jobGrades,
        List<EmployeeItem> employees,
        List<CostCategoryItem> costCategories,
        List<ProjectItem> projects,
        List<ProjectMemberItem> projectMembers,
        List<ClosingPeriodItem> closingPeriods,
        UUID focusedClosingPeriodId,
        String focusedClosingPeriodYearMonth,
        List<CostEntryItem> costEntries,
        List<StandardLaborRateItem> standardLaborRates,
        List<StandardCostResultItem> standardCostResults,
        List<AllocationRuleItem> allocationRules,
        List<AllocationExecutionItem> allocationExecutions,
        List<AllocationResultItem> allocationResults,
        List<TransferPriceResultItem> transferPriceResults
    ) {
    }

    public record HeadquartersItem(UUID id, String code, String name, boolean active) {
    }

    public record DepartmentItem(
        UUID id,
        UUID headquartersId,
        String headquartersCode,
        String headquartersName,
        String code,
        String name,
        boolean active
    ) {
    }

    public record JobGradeItem(UUID id, String code, String name, int sortOrder, boolean active) {
    }

    public record EmployeeItem(
        UUID id,
        UUID departmentId,
        String departmentName,
        UUID jobGradeId,
        String jobGradeName,
        String employeeNo,
        String name,
        String employmentType,
        boolean active
    ) {
    }

    public record CostCategoryItem(UUID id, String code, String name, String categoryGroup, boolean active) {
    }

    public record ProjectItem(
        UUID id,
        UUID departmentId,
        String departmentCode,
        String departmentName,
        String code,
        String name,
        String projectType,
        String status,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal plannedRevenue,
        BigDecimal plannedCost
    ) {
    }

    public record ProjectMemberItem(
        UUID id,
        UUID projectId,
        String projectCode,
        String projectName,
        UUID employeeId,
        String employeeNo,
        String employeeName,
        String departmentName,
        String jobGradeName,
        String role,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal allocationRate,
        BigDecimal monthlyWorkHours,
        boolean active
    ) {
    }

    public record ClosingPeriodItem(UUID id, String yearMonth, ClosingStatus status, long version) {
    }

    public record CostEntryItem(
        UUID id,
        LocalDate occurredDate,
        String costCategoryCode,
        String costCategoryName,
        CostType costType,
        CostScope costScope,
        BigDecimal amount,
        String description,
        String projectCode,
        String projectName,
        String departmentName,
        String headquartersName,
        String employeeName,
        CostSourceType sourceType,
        String sourceReference
    ) implements CostEntryItemSource {
    }

    public record StandardLaborRateItem(
        UUID id,
        UUID jobGradeId,
        String jobGradeCode,
        String jobGradeName,
        BigDecimal hourlyRate,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        boolean active
    ) {
    }

    public record StandardCostResultItem(
        UUID id,
        String projectCode,
        String projectName,
        BigDecimal standardLaborCost,
        BigDecimal standardCommonCost,
        BigDecimal totalStandardCost,
        int calculationVersion
    ) {
    }

    public record AllocationRuleItem(
        UUID id,
        String name,
        AllocationType allocationType,
        CostScope sourceScope,
        CostScope targetScope,
        boolean active
    ) {
    }

    public record AllocationExecutionItem(
        UUID id,
        String allocationRuleName,
        int executionVersion,
        AllocationExecutionStatus status,
        String failureReason,
        String idempotencyKey
    ) {
    }

    public record AllocationResultItem(
        UUID id,
        int executionVersion,
        AllocationExecutionStatus executionStatus,
        String allocationRuleName,
        String sourceDescription,
        String targetProjectCode,
        String targetProjectName,
        BigDecimal basisValue,
        BigDecimal allocationRate,
        BigDecimal allocationRatePercentage,
        BigDecimal allocatedAmount,
        BigDecimal adjustmentAmount
    ) {
    }

    public record TransferPriceResultItem(
        UUID id,
        String projectCode,
        String projectName,
        String employeeNo,
        String employeeName,
        String departmentName,
        String jobGradeName,
        BigDecimal workHours,
        BigDecimal hourlyRate,
        BigDecimal transferAmount,
        int calculationVersion
    ) {
    }

    public interface CostEntryItemSource {
        LocalDate occurredDate();

        String description();
    }
}

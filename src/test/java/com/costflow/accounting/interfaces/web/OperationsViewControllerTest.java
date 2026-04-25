package com.costflow.accounting.interfaces.web;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.costflow.accounting.application.facade.OperationsWorkspaceFacade;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.AllocationExecutionItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.AllocationResultItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.AllocationRuleItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.ClosingPeriodItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.CostCategoryItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.CostEntryItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.DepartmentItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.EmployeeItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.HeadquartersItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.JobGradeItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.OperationsWorkspaceData;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.ProjectItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.ProjectMemberItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.StandardCostResultItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.StandardLaborRateItem;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.TransferPriceResultItem;
import com.costflow.accounting.domain.allocation.AllocationExecution;
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
import com.costflow.accounting.domain.standardcost.StandardCostResult;
import com.costflow.accounting.domain.standardcost.StandardLaborRate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OperationsViewController.class)
class OperationsViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationsWorkspaceFacade operationsWorkspaceFacade;

    @Test
    void operations_조회시_운영콘솔화면을_렌더링한다() throws Exception {
        final UUID closingPeriodId = UUID.randomUUID();
        when(operationsWorkspaceFacade.getWorkspaceData(closingPeriodId))
            .thenReturn(workspaceData(closingPeriodId, "2026-04"));

        mockMvc.perform(get("/operations").param("closingPeriodId", closingPeriodId.toString()))
            .andExpect(status().isOk())
            .andExpect(view().name("operations"))
            .andExpect(model().attributeExists("view"));

        verify(operationsWorkspaceFacade).getWorkspaceData(closingPeriodId);
    }

    @Test
    void createHeadquarters_성공시_기본경로로_리다이렉트한다() throws Exception {
        when(operationsWorkspaceFacade.createHeadquarters("HQ-001", "Delivery HQ"))
            .thenReturn(new Headquarters("HQ-001", "Delivery HQ"));

        mockMvc.perform(post("/operations/headquarters")
                .param("code", "HQ-001")
                .param("name", "Delivery HQ"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations"))
            .andExpect(flash().attribute("notice", "본부를 등록했습니다."));

        verify(operationsWorkspaceFacade).createHeadquarters("HQ-001", "Delivery HQ");
    }

    @Test
    void createDepartment_성공시_선택마감월로_리다이렉트한다() throws Exception {
        final UUID headquartersId = UUID.randomUUID();
        final UUID focusedClosingPeriodId = UUID.randomUUID();
        when(operationsWorkspaceFacade.createDepartment(headquartersId, "DEP-001", "Platform Team"))
            .thenReturn(new Department(new Headquarters("HQ-001", "Delivery HQ"), "DEP-001", "Platform Team"));

        mockMvc.perform(post("/operations/departments")
                .param("headquartersId", headquartersId.toString())
                .param("code", "DEP-001")
                .param("name", "Platform Team")
                .param("focusedClosingPeriodId", focusedClosingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + focusedClosingPeriodId))
            .andExpect(flash().attribute("notice", "부서를 등록했습니다."));

        verify(operationsWorkspaceFacade).createDepartment(headquartersId, "DEP-001", "Platform Team");
    }

    @Test
    void createJobGrade_성공시_선택마감월로_리다이렉트한다() throws Exception {
        final UUID focusedClosingPeriodId = UUID.randomUUID();
        when(operationsWorkspaceFacade.createJobGrade("G4", "Senior", 10))
            .thenReturn(new JobGrade("G4", "Senior", 10));

        mockMvc.perform(post("/operations/job-grades")
                .param("code", "G4")
                .param("name", "Senior")
                .param("sortOrder", "10")
                .param("focusedClosingPeriodId", focusedClosingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + focusedClosingPeriodId))
            .andExpect(flash().attribute("notice", "직급을 등록했습니다."));

        verify(operationsWorkspaceFacade).createJobGrade("G4", "Senior", 10);
    }

    @Test
    void createEmployee_성공시_선택마감월로_리다이렉트한다() throws Exception {
        final UUID departmentId = UUID.randomUUID();
        final UUID jobGradeId = UUID.randomUUID();
        final UUID focusedClosingPeriodId = UUID.randomUUID();
        when(operationsWorkspaceFacade.createEmployee(departmentId, jobGradeId, "EMP-001", "홍길동", "정규직"))
            .thenReturn(new Employee(
                new Department(new Headquarters("HQ-001", "Delivery HQ"), "DEP-001", "Platform Team"),
                new JobGrade("G4", "Senior", 10),
                "EMP-001",
                "홍길동",
                "정규직"
            ));

        mockMvc.perform(post("/operations/employees")
                .param("departmentId", departmentId.toString())
                .param("jobGradeId", jobGradeId.toString())
                .param("employeeNo", "EMP-001")
                .param("name", "홍길동")
                .param("employmentType", "정규직")
                .param("focusedClosingPeriodId", focusedClosingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + focusedClosingPeriodId))
            .andExpect(flash().attribute("notice", "인력을 등록했습니다."));

        verify(operationsWorkspaceFacade).createEmployee(departmentId, jobGradeId, "EMP-001", "홍길동", "정규직");
    }

    @Test
    void createCostCategory_성공시_선택마감월로_리다이렉트한다() throws Exception {
        final UUID focusedClosingPeriodId = UUID.randomUUID();
        when(operationsWorkspaceFacade.createCostCategory("COST-001", "개발 인건비", "LABOR"))
            .thenReturn(null);

        mockMvc.perform(post("/operations/cost-categories")
                .param("code", "COST-001")
                .param("name", "개발 인건비")
                .param("categoryGroup", "LABOR")
                .param("focusedClosingPeriodId", focusedClosingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + focusedClosingPeriodId))
            .andExpect(flash().attribute("notice", "비용 항목을 등록했습니다."));

        verify(operationsWorkspaceFacade).createCostCategory("COST-001", "개발 인건비", "LABOR");
    }

    @Test
    void createProject_성공시_선택마감월로_리다이렉트한다() throws Exception {
        final UUID departmentId = UUID.randomUUID();
        final UUID focusedClosingPeriodId = UUID.randomUUID();
        when(operationsWorkspaceFacade.createProject(
            departmentId,
            "PJT-001",
            "원가관리 구축",
            "SI",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            new BigDecimal("10000000.00"),
            new BigDecimal("8000000.00")
        )).thenReturn(new Project(
            new Department(new Headquarters("HQ-001", "Delivery HQ"), "DEP-001", "Platform Team"),
            "PJT-001",
            "원가관리 구축",
            "SI",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            new BigDecimal("10000000.00"),
            new BigDecimal("8000000.00")
        ));

        mockMvc.perform(post("/operations/projects")
                .param("departmentId", departmentId.toString())
                .param("code", "PJT-001")
                .param("name", "원가관리 구축")
                .param("projectType", "SI")
                .param("startDate", "2026-04-01")
                .param("endDate", "2026-04-30")
                .param("plannedRevenue", "10000000.00")
                .param("plannedCost", "8000000.00")
                .param("focusedClosingPeriodId", focusedClosingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + focusedClosingPeriodId))
            .andExpect(flash().attribute("notice", "프로젝트를 등록했습니다."));

        verify(operationsWorkspaceFacade).createProject(
            departmentId,
            "PJT-001",
            "원가관리 구축",
            "SI",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            new BigDecimal("10000000.00"),
            new BigDecimal("8000000.00")
        );
    }

    @Test
    void createProjectMember_성공시_선택마감월로_리다이렉트한다() throws Exception {
        final UUID projectId = UUID.randomUUID();
        final UUID employeeId = UUID.randomUUID();
        final UUID focusedClosingPeriodId = UUID.randomUUID();
        final Department department = new Department(new Headquarters("HQ-001", "Delivery HQ"), "DEP-001", "Platform Team");
        final JobGrade jobGrade = new JobGrade("G4", "Senior", 10);
        final Employee employee = new Employee(department, jobGrade, "EMP-001", "홍길동", "정규직");
        final Project project = new Project(
            department,
            "PJT-001",
            "원가관리 구축",
            "SI",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            new BigDecimal("10000000.00"),
            new BigDecimal("8000000.00")
        );
        when(operationsWorkspaceFacade.createProjectMember(
            projectId,
            employeeId,
            "Developer",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            new BigDecimal("100.00"),
            new BigDecimal("160.00")
        )).thenReturn(new ProjectMember(
            project,
            employee,
            "Developer",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            new BigDecimal("100.00"),
            new BigDecimal("160.00")
        ));

        mockMvc.perform(post("/operations/project-members")
                .param("projectId", projectId.toString())
                .param("employeeId", employeeId.toString())
                .param("role", "Developer")
                .param("startDate", "2026-04-01")
                .param("endDate", "2026-04-30")
                .param("allocationRate", "100.00")
                .param("monthlyWorkHours", "160.00")
                .param("focusedClosingPeriodId", focusedClosingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + focusedClosingPeriodId))
            .andExpect(flash().attribute("notice", "프로젝트 참여 인력을 등록했습니다."));

        verify(operationsWorkspaceFacade).createProjectMember(
            projectId,
            employeeId,
            "Developer",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            new BigDecimal("100.00"),
            new BigDecimal("160.00")
        );
    }

    @Test
    void createClosingPeriod_성공시_생성한마감월로_리다이렉트한다() throws Exception {
        final UUID createdClosingPeriodId = UUID.randomUUID();
        final ClosingPeriod createdClosingPeriod = new ClosingPeriod("2026-04");
        setId(createdClosingPeriod, createdClosingPeriodId);
        when(operationsWorkspaceFacade.createClosingPeriod("2026-04"))
            .thenReturn(createdClosingPeriod);

        mockMvc.perform(post("/operations/closing-periods")
                .param("yearMonth", "2026-04"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + createdClosingPeriodId))
            .andExpect(flash().attribute("notice", "마감 월을 등록했습니다."));

        verify(operationsWorkspaceFacade).createClosingPeriod("2026-04");
    }

    @Test
    void createCostEntry_실패시_오류메시지와함께_선택마감월로_리다이렉트한다() throws Exception {
        final UUID closingPeriodId = UUID.randomUUID();
        final UUID costCategoryId = UUID.randomUUID();
        doThrow(new RuntimeException("비용 등록 실패"))
            .when(operationsWorkspaceFacade)
            .createCostEntry(
                eq(closingPeriodId),
                eq(costCategoryId),
                eq(CostType.COMMON),
                eq(CostScope.COMPANY),
                isNull(),
                isNull(),
                isNull(),
                isNull(),
                eq(new BigDecimal("1500000.00")),
                eq(LocalDate.of(2026, 4, 25)),
                eq("전사 공통비"),
                eq(CostSourceType.MANUAL),
                eq("ERP-001")
            );

        mockMvc.perform(post("/operations/cost-entries")
                .param("closingPeriodId", closingPeriodId.toString())
                .param("costCategoryId", costCategoryId.toString())
                .param("costType", CostType.COMMON.name())
                .param("costScope", CostScope.COMPANY.name())
                .param("amount", "1500000.00")
                .param("occurredDate", "2026-04-25")
                .param("description", "전사 공통비")
                .param("sourceType", CostSourceType.MANUAL.name())
                .param("sourceReference", "ERP-001"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + closingPeriodId))
            .andExpect(flash().attribute("error", "비용 등록 실패"));
    }

    @Test
    void calculateStandardCosts_성공시_생성건수를_안내한다() throws Exception {
        final UUID closingPeriodId = UUID.randomUUID();
        final Headquarters headquarters = new Headquarters("HQ-001", "Delivery HQ");
        final Department department = new Department(headquarters, "DEP-001", "Platform Team");
        final Project project = new Project(
            department,
            "PJT-001",
            "원가관리 구축",
            "SI",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            BigDecimal.valueOf(10_000_000L),
            BigDecimal.valueOf(8_000_000L)
        );
        when(operationsWorkspaceFacade.calculateStandardCosts(closingPeriodId))
            .thenReturn(List.of(
                new StandardCostResult(new ClosingPeriod("2026-04"), project, BigDecimal.valueOf(1_000_000L), BigDecimal.ZERO, 1),
                new StandardCostResult(new ClosingPeriod("2026-04"), project, BigDecimal.valueOf(2_000_000L), BigDecimal.ZERO, 2)
            ));

        mockMvc.perform(post("/operations/standard-costs/calculate")
                .param("closingPeriodId", closingPeriodId.toString())
                .param("focusedClosingPeriodId", closingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + closingPeriodId))
            .andExpect(flash().attribute("notice", "표준원가 계산을 완료했습니다. 생성 건수: 2"));

        verify(operationsWorkspaceFacade).calculateStandardCosts(closingPeriodId);
    }

    @Test
    void transitionClosingPeriod_성공시_대상마감월로_리다이렉트한다() throws Exception {
        final UUID closingPeriodId = UUID.randomUUID();
        when(operationsWorkspaceFacade.transitionClosingPeriod(closingPeriodId, ClosingStatus.CALCULATED))
            .thenReturn(new ClosingPeriod("2026-04"));

        mockMvc.perform(post("/operations/closing-periods/status")
                .param("closingPeriodId", closingPeriodId.toString())
                .param("nextStatus", ClosingStatus.CALCULATED.name()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + closingPeriodId))
            .andExpect(flash().attribute("notice", "마감 상태를 변경했습니다."));

        verify(operationsWorkspaceFacade).transitionClosingPeriod(closingPeriodId, ClosingStatus.CALCULATED);
    }

    @Test
    void createStandardLaborRate_성공시_선택마감월로_리다이렉트한다() throws Exception {
        final UUID jobGradeId = UUID.randomUUID();
        final UUID focusedClosingPeriodId = UUID.randomUUID();
        when(operationsWorkspaceFacade.createStandardLaborRate(
            jobGradeId,
            new BigDecimal("125000.00"),
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30)
        )).thenReturn(null);

        mockMvc.perform(post("/operations/standard-labor-rates")
                .param("jobGradeId", jobGradeId.toString())
                .param("hourlyRate", "125000.00")
                .param("effectiveFrom", "2026-04-01")
                .param("effectiveTo", "2026-04-30")
                .param("focusedClosingPeriodId", focusedClosingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + focusedClosingPeriodId))
            .andExpect(flash().attribute("notice", "표준노무단가를 등록했습니다."));

        verify(operationsWorkspaceFacade).createStandardLaborRate(
            jobGradeId,
            new BigDecimal("125000.00"),
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30)
        );
    }

    @Test
    void createAllocationRule_실패시_기본오류메시지를_노출한다() throws Exception {
        final UUID focusedClosingPeriodId = UUID.randomUUID();
        doThrow(new RuntimeException("   "))
            .when(operationsWorkspaceFacade)
            .createAllocationRule("전사 공통비 배부", AllocationType.HOURS, CostScope.COMPANY);

        mockMvc.perform(post("/operations/allocation-rules")
                .param("name", "전사 공통비 배부")
                .param("allocationType", AllocationType.HOURS.name())
                .param("sourceScope", CostScope.COMPANY.name())
                .param("focusedClosingPeriodId", focusedClosingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + focusedClosingPeriodId))
            .andExpect(flash().attribute("error", "배부 기준 등록에 실패했습니다."));

        verify(operationsWorkspaceFacade).createAllocationRule("전사 공통비 배부", AllocationType.HOURS, CostScope.COMPANY);
    }

    @Test
    void calculateTransferPrices_성공시_생성건수를_안내한다() throws Exception {
        final UUID closingPeriodId = UUID.randomUUID();
        final Headquarters headquarters = new Headquarters("HQ-001", "Delivery HQ");
        final Department department = new Department(headquarters, "DEP-001", "Platform Team");
        final JobGrade jobGrade = new JobGrade("G4", "Senior", 10);
        final Employee employee = new Employee(department, jobGrade, "EMP-001", "홍길동", "정규직");
        final Project project = new Project(
            department,
            "PJT-001",
            "원가관리 구축",
            "SI",
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30),
            BigDecimal.valueOf(10_000_000L),
            BigDecimal.valueOf(8_000_000L)
        );
        final StandardLaborRate standardLaborRate = new StandardLaborRate(
            jobGrade,
            new BigDecimal("125000.00"),
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 4, 30)
        );
        when(operationsWorkspaceFacade.calculateTransferPrices(closingPeriodId))
            .thenReturn(List.of(
                new com.costflow.accounting.domain.transferprice.TransferPriceResult(
                    new ClosingPeriod("2026-04"),
                    project,
                    employee,
                    department,
                    standardLaborRate,
                    new BigDecimal("160.00"),
                    new BigDecimal("125000.00"),
                    new BigDecimal("20000000.00"),
                    1
                ),
                new com.costflow.accounting.domain.transferprice.TransferPriceResult(
                    new ClosingPeriod("2026-04"),
                    project,
                    employee,
                    department,
                    standardLaborRate,
                    new BigDecimal("80.00"),
                    new BigDecimal("125000.00"),
                    new BigDecimal("10000000.00"),
                    2
                ),
                new com.costflow.accounting.domain.transferprice.TransferPriceResult(
                    new ClosingPeriod("2026-04"),
                    project,
                    employee,
                    department,
                    standardLaborRate,
                    new BigDecimal("40.00"),
                    new BigDecimal("125000.00"),
                    new BigDecimal("5000000.00"),
                    3
                )
            ));

        mockMvc.perform(post("/operations/transfer-prices/calculate")
                .param("closingPeriodId", closingPeriodId.toString())
                .param("focusedClosingPeriodId", closingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + closingPeriodId))
            .andExpect(flash().attribute("notice", "내부대체가액 계산을 완료했습니다. 생성 건수: 3"));

        verify(operationsWorkspaceFacade).calculateTransferPrices(closingPeriodId);
    }

    @Test
    void executeAllocation_성공시_실행버전을_안내한다() throws Exception {
        final UUID closingPeriodId = UUID.randomUUID();
        final UUID allocationRuleId = UUID.randomUUID();
        final AllocationExecution execution = new AllocationExecution(
            new ClosingPeriod("2026-04"),
            new AllocationRule("전사 공통비 배부", AllocationType.HOURS, CostScope.COMPANY, CostScope.PROJECT),
            3,
            "allocation-3"
        );
        when(operationsWorkspaceFacade.executeAllocation(closingPeriodId, allocationRuleId, "allocation-3"))
            .thenReturn(execution);

        mockMvc.perform(post("/operations/allocation-executions")
                .param("closingPeriodId", closingPeriodId.toString())
                .param("allocationRuleId", allocationRuleId.toString())
                .param("idempotencyKey", "allocation-3")
                .param("focusedClosingPeriodId", closingPeriodId.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/operations?closingPeriodId=" + closingPeriodId))
            .andExpect(flash().attribute("notice", "배부 실행을 요청했습니다. 실행 버전: 3"));

        verify(operationsWorkspaceFacade).executeAllocation(closingPeriodId, allocationRuleId, "allocation-3");
    }

    private OperationsWorkspaceData workspaceData(final UUID focusedClosingPeriodId, final String yearMonth) {
        return new OperationsWorkspaceData(
            List.<HeadquartersItem>of(),
            List.<DepartmentItem>of(),
            List.<JobGradeItem>of(),
            List.<EmployeeItem>of(),
            List.<CostCategoryItem>of(),
            List.<ProjectItem>of(),
            List.<ProjectMemberItem>of(),
            List.of(new ClosingPeriodItem(focusedClosingPeriodId, yearMonth, ClosingStatus.OPEN, 0L)),
            focusedClosingPeriodId,
            yearMonth,
            List.<CostEntryItem>of(),
            List.<StandardLaborRateItem>of(),
            List.<StandardCostResultItem>of(),
            List.<AllocationRuleItem>of(),
            List.<AllocationExecutionItem>of(),
            List.<AllocationResultItem>of(),
            List.<TransferPriceResultItem>of()
        );
    }

    private void setId(final ClosingPeriod closingPeriod, final UUID id) throws Exception {
        final var field = ClosingPeriod.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(closingPeriod, id);
    }
}

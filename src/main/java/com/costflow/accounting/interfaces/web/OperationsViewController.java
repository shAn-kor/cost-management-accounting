package com.costflow.accounting.interfaces.web;

import com.costflow.accounting.application.facade.OperationsWorkspaceFacade;
import com.costflow.accounting.application.facade.OperationsWorkspaceFacade.OperationsWorkspaceData;
import com.costflow.accounting.domain.allocation.AllocationType;
import com.costflow.accounting.domain.closing.ClosingStatus;
import com.costflow.accounting.domain.cost.CostScope;
import com.costflow.accounting.domain.cost.CostSourceType;
import com.costflow.accounting.domain.cost.CostType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/operations")
public class OperationsViewController {

    private final OperationsWorkspaceFacade operationsWorkspaceFacade;

    public OperationsViewController(final OperationsWorkspaceFacade operationsWorkspaceFacade) {
        this.operationsWorkspaceFacade = operationsWorkspaceFacade;
    }

    @GetMapping
    public String operations(@RequestParam(required = false) final UUID closingPeriodId, final Model model) {
        final OperationsWorkspaceData view = operationsWorkspaceFacade.getWorkspaceData(closingPeriodId);
        model.addAttribute("view", view);
        return "operations";
    }

    @PostMapping("/headquarters")
    public String createHeadquarters(
        @RequestParam final String code,
        @RequestParam final String name,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createHeadquarters(code, name);
            redirectAttributes.addFlashAttribute("notice", "본부를 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "본부 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/departments")
    public String createDepartment(
        @RequestParam final UUID headquartersId,
        @RequestParam final String code,
        @RequestParam final String name,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createDepartment(headquartersId, code, name);
            redirectAttributes.addFlashAttribute("notice", "부서를 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "부서 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/job-grades")
    public String createJobGrade(
        @RequestParam final String code,
        @RequestParam final String name,
        @RequestParam final int sortOrder,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createJobGrade(code, name, sortOrder);
            redirectAttributes.addFlashAttribute("notice", "직급을 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "직급 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/employees")
    public String createEmployee(
        @RequestParam final UUID departmentId,
        @RequestParam final UUID jobGradeId,
        @RequestParam final String employeeNo,
        @RequestParam final String name,
        @RequestParam final String employmentType,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createEmployee(departmentId, jobGradeId, employeeNo, name, employmentType);
            redirectAttributes.addFlashAttribute("notice", "인력을 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "인력 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/cost-categories")
    public String createCostCategory(
        @RequestParam final String code,
        @RequestParam final String name,
        @RequestParam final String categoryGroup,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createCostCategory(code, name, categoryGroup);
            redirectAttributes.addFlashAttribute("notice", "비용 항목을 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "비용 항목 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/projects")
    public String createProject(
        @RequestParam final UUID departmentId,
        @RequestParam final String code,
        @RequestParam final String name,
        @RequestParam final String projectType,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate,
        @RequestParam final BigDecimal plannedRevenue,
        @RequestParam final BigDecimal plannedCost,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createProject(departmentId, code, name, projectType, startDate, endDate, plannedRevenue, plannedCost);
            redirectAttributes.addFlashAttribute("notice", "프로젝트를 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "프로젝트 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/project-members")
    public String createProjectMember(
        @RequestParam final UUID projectId,
        @RequestParam final UUID employeeId,
        @RequestParam final String role,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate,
        @RequestParam final BigDecimal allocationRate,
        @RequestParam final BigDecimal monthlyWorkHours,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createProjectMember(projectId, employeeId, role, startDate, endDate, allocationRate, monthlyWorkHours);
            redirectAttributes.addFlashAttribute("notice", "프로젝트 참여 인력을 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "프로젝트 참여 인력 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/closing-periods")
    public String createClosingPeriod(
        @RequestParam final String yearMonth,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            final var closingPeriod = operationsWorkspaceFacade.createClosingPeriod(yearMonth);
            redirectAttributes.addFlashAttribute("notice", "마감 월을 등록했습니다.");
            return redirectToOperations(closingPeriod.getId());
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "마감 월 등록에 실패했습니다."));
            return redirectToOperations(focusedClosingPeriodId);
        }
    }

    @PostMapping("/closing-periods/status")
    public String transitionClosingPeriod(
        @RequestParam final UUID closingPeriodId,
        @RequestParam final ClosingStatus nextStatus,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.transitionClosingPeriod(closingPeriodId, nextStatus);
            redirectAttributes.addFlashAttribute("notice", "마감 상태를 변경했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "마감 상태 변경에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId == null ? closingPeriodId : focusedClosingPeriodId);
    }

    @PostMapping("/cost-entries")
    public String createCostEntry(
        @RequestParam final UUID closingPeriodId,
        @RequestParam final UUID costCategoryId,
        @RequestParam final CostType costType,
        @RequestParam final CostScope costScope,
        @RequestParam(required = false) final UUID projectId,
        @RequestParam(required = false) final UUID departmentId,
        @RequestParam(required = false) final UUID headquartersId,
        @RequestParam(required = false) final UUID employeeId,
        @RequestParam final BigDecimal amount,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate occurredDate,
        @RequestParam final String description,
        @RequestParam final CostSourceType sourceType,
        @RequestParam(required = false) final String sourceReference,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createCostEntry(
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
            );
            redirectAttributes.addFlashAttribute("notice", "비용을 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "비용 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId == null ? closingPeriodId : focusedClosingPeriodId);
    }

    @PostMapping("/standard-labor-rates")
    public String createStandardLaborRate(
        @RequestParam final UUID jobGradeId,
        @RequestParam final BigDecimal hourlyRate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate effectiveFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate effectiveTo,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createStandardLaborRate(jobGradeId, hourlyRate, effectiveFrom, effectiveTo);
            redirectAttributes.addFlashAttribute("notice", "표준노무단가를 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "표준노무단가 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/standard-costs/calculate")
    public String calculateStandardCosts(
        @RequestParam final UUID closingPeriodId,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            final int resultCount = operationsWorkspaceFacade.calculateStandardCosts(closingPeriodId).size();
            redirectAttributes.addFlashAttribute("notice", "표준원가 계산을 완료했습니다. 생성 건수: " + resultCount);
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "표준원가 계산에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId == null ? closingPeriodId : focusedClosingPeriodId);
    }

    @PostMapping("/allocation-rules")
    public String createAllocationRule(
        @RequestParam final String name,
        @RequestParam final AllocationType allocationType,
        @RequestParam final CostScope sourceScope,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            operationsWorkspaceFacade.createAllocationRule(name, allocationType, sourceScope);
            redirectAttributes.addFlashAttribute("notice", "배부 기준을 등록했습니다.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "배부 기준 등록에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId);
    }

    @PostMapping("/allocation-executions")
    public String executeAllocation(
        @RequestParam final UUID closingPeriodId,
        @RequestParam final UUID allocationRuleId,
        @RequestParam(required = false) final String idempotencyKey,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            final var execution = operationsWorkspaceFacade.executeAllocation(closingPeriodId, allocationRuleId, idempotencyKey);
            redirectAttributes.addFlashAttribute("notice", "배부 실행을 요청했습니다. 실행 버전: " + execution.getExecutionVersion());
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "배부 실행에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId == null ? closingPeriodId : focusedClosingPeriodId);
    }

    @PostMapping("/transfer-prices/calculate")
    public String calculateTransferPrices(
        @RequestParam final UUID closingPeriodId,
        @RequestParam(required = false) final UUID focusedClosingPeriodId,
        final RedirectAttributes redirectAttributes
    ) {
        try {
            final int resultCount = operationsWorkspaceFacade.calculateTransferPrices(closingPeriodId).size();
            redirectAttributes.addFlashAttribute("notice", "내부대체가액 계산을 완료했습니다. 생성 건수: " + resultCount);
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("error", messageOf(exception, "내부대체가액 계산에 실패했습니다."));
        }
        return redirectToOperations(focusedClosingPeriodId == null ? closingPeriodId : focusedClosingPeriodId);
    }

    private String redirectToOperations(final UUID focusedClosingPeriodId) {
        if (focusedClosingPeriodId == null) {
            return "redirect:/operations";
        }
        return "redirect:/operations?closingPeriodId=" + focusedClosingPeriodId;
    }

    private String messageOf(final RuntimeException exception, final String defaultMessage) {
        return exception.getMessage() == null || exception.getMessage().isBlank() ? defaultMessage : exception.getMessage();
    }
}

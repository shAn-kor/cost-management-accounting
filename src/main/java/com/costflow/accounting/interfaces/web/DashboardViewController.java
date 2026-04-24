package com.costflow.accounting.interfaces.web;

import com.costflow.accounting.application.facade.DashboardViewFacade;
import com.costflow.accounting.application.facade.DashboardViewFacade.DashboardViewData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardViewController {

    private final DashboardViewFacade dashboardViewFacade;

    public DashboardViewController(final DashboardViewFacade dashboardViewFacade) {
        this.dashboardViewFacade = dashboardViewFacade;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(final Model model) {
        final DashboardViewData viewData = dashboardViewFacade.getDashboardViewData();
        model.addAttribute("headquartersCount", viewData.headquartersCount());
        model.addAttribute("departmentCount", viewData.departmentCount());
        model.addAttribute("employeeCount", viewData.employeeCount());
        model.addAttribute("projectCount", viewData.projectCount());
        model.addAttribute("costCategoryCount", viewData.costCategoryCount());
        model.addAttribute("closingPeriods", viewData.closingPeriods());
        model.addAttribute("projects", viewData.projects());
        return "dashboard";
    }
}

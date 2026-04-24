package com.costflow.accounting.domain.project;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.organization.Headquarters;
import com.costflow.accounting.domain.organization.JobGrade;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ProjectMemberTest {

    @Test
    void createProjectMember_프로젝트기간안이면_생성한다() {
        final Project project = project();
        final Employee employee = employee();

        new ProjectMember(
            project,
            employee,
            "DEV",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 6, 30),
            BigDecimal.valueOf(50),
            BigDecimal.valueOf(80)
        );
    }

    @Test
    void createProjectMember_프로젝트기간밖이면_실패한다() {
        final Project project = project();
        final Employee employee = employee();

        assertThatThrownBy(() -> new ProjectMember(
            project,
            employee,
            "DEV",
            LocalDate.of(2025, 12, 31),
            LocalDate.of(2026, 6, 30),
            BigDecimal.valueOf(50),
            BigDecimal.valueOf(80)
        )).isInstanceOf(ValidationException.class);
    }

    @Test
    void createProjectMember_투입률이0이면_실패한다() {
        final Project project = project();
        final Employee employee = employee();

        assertThatThrownBy(() -> new ProjectMember(
            project,
            employee,
            "DEV",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 6, 30),
            BigDecimal.ZERO,
            BigDecimal.valueOf(80)
        )).isInstanceOf(ValidationException.class);
    }

    @Test
    void createProjectMember_월투입시간이음수이면_실패한다() {
        final Project project = project();
        final Employee employee = employee();

        assertThatThrownBy(() -> new ProjectMember(
            project,
            employee,
            "DEV",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 6, 30),
            BigDecimal.valueOf(50),
            BigDecimal.valueOf(-1)
        )).isInstanceOf(ValidationException.class);
    }

    private Project project() {
        return new Project(
            department(),
            "P-001",
            "정산 시스템 구축",
            "SI",
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31),
            BigDecimal.valueOf(100_000_000),
            BigDecimal.valueOf(80_000_000)
        );
    }

    private Employee employee() {
        return new Employee(department(), jobGrade(), "E-001", "홍길동", "FULL_TIME");
    }

    private Department department() {
        return new Department(new Headquarters("HQ-001", "플랫폼본부"), "D-001", "개발부");
    }

    private JobGrade jobGrade() {
        return new JobGrade("JG-001", "선임", 1);
    }
}

package com.costflow.accounting.domain.organization;

import com.costflow.accounting.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_grade_id", nullable = false)
    private JobGrade jobGrade;

    @Column(nullable = false, unique = true, length = 30)
    private String employeeNo;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 30)
    private String employmentType;

    @Column(nullable = false)
    private boolean active;

    protected Employee() {
    }

    public Employee(final Department department, final JobGrade jobGrade, final String employeeNo, final String name, final String employmentType) {
        this.department = department;
        this.jobGrade = jobGrade;
        this.employeeNo = employeeNo;
        this.name = name;
        this.employmentType = employmentType;
        active = true;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Department getDepartment() {
        return department;
    }

    public JobGrade getJobGrade() {
        return jobGrade;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public String getName() {
        return name;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public boolean isActive() {
        return active;
    }

    public void update(final Department department, final JobGrade jobGrade, final String name, final String employmentType) {
        this.department = department;
        this.jobGrade = jobGrade;
        this.name = name;
        this.employmentType = employmentType;
    }

    public void changeActive(final boolean active) {
        this.active = active;
    }
}

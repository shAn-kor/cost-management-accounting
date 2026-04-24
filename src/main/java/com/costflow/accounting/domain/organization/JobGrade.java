package com.costflow.accounting.domain.organization;

import com.costflow.accounting.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "job_grades")
public class JobGrade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean active;

    protected JobGrade() {
    }

    public JobGrade(final String code, final String name, final int sortOrder) {
        this.code = code;
        this.name = name;
        this.sortOrder = sortOrder;
        active = true;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public boolean isActive() {
        return active;
    }

    public void update(final String name, final int sortOrder) {
        this.name = name;
        this.sortOrder = sortOrder;
    }

    public void changeActive(final boolean active) {
        this.active = active;
    }
}

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
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Table(
    name = "departments",
    uniqueConstraints = @UniqueConstraint(name = "uk_departments_headquarters_code", columnNames = {"headquarters_id", "code"})
)
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "headquarters_id", nullable = false)
    private Headquarters headquarters;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private boolean active;

    protected Department() {
    }

    public Department(final Headquarters headquarters, final String code, final String name) {
        this.headquarters = headquarters;
        this.code = code;
        this.name = name;
        active = true;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Headquarters getHeadquarters() {
        return headquarters;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void update(final String name) {
        this.name = name;
    }

    public void changeActive(final boolean active) {
        this.active = active;
    }
}

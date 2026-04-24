package com.costflow.accounting.domain.cost;

import com.costflow.accounting.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "cost_categories")
public class CostCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 30)
    private String categoryGroup;

    @Column(nullable = false)
    private boolean active;

    protected CostCategory() {
    }

    public CostCategory(final String code, final String name, final String categoryGroup) {
        this.code = code;
        this.name = name;
        this.categoryGroup = categoryGroup;
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

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public boolean isActive() {
        return active;
    }

    public void update(final String name, final String categoryGroup) {
        this.name = name;
        this.categoryGroup = categoryGroup;
    }

    public void changeActive(final boolean active) {
        this.active = active;
    }
}

package com.costflow.accounting.application.cost;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.cost.CostCategory;
import com.costflow.accounting.domain.cost.CostCategoryRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CostCategoryApplicationService {

    private final CostCategoryRepository costCategoryRepository;

    public CostCategoryApplicationService(final CostCategoryRepository costCategoryRepository) {
        this.costCategoryRepository = costCategoryRepository;
    }

    @Transactional
    public CostCategory create(final CreateCostCategoryCommand command) {
        return costCategoryRepository.save(new CostCategory(command.code(), command.name(), command.categoryGroup()));
    }

    @Transactional
    public CostCategory update(final UUID costCategoryId, final UpdateCostCategoryCommand command) {
        final CostCategory costCategory = get(costCategoryId);
        costCategory.update(command.name(), command.categoryGroup());
        return costCategory;
    }

    @Transactional
    public CostCategory changeActive(final UUID costCategoryId, final boolean active) {
        final CostCategory costCategory = get(costCategoryId);
        costCategory.changeActive(active);
        return costCategory;
    }

    @Transactional(readOnly = true)
    public CostCategory get(final UUID costCategoryId) {
        return costCategoryRepository.findById(costCategoryId)
            .orElseThrow(() -> new NotFoundException("비용 항목을 찾을 수 없습니다. id=" + costCategoryId));
    }

    @Transactional(readOnly = true)
    public List<CostCategory> getAll() {
        return costCategoryRepository.findAll();
    }
}

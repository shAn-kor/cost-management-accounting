package com.costflow.accounting.application.cost;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.cost.CostCategory;
import com.costflow.accounting.domain.cost.CostEntry;
import com.costflow.accounting.domain.cost.CostEntryRepository;
import com.costflow.accounting.domain.cost.CostScope;
import com.costflow.accounting.domain.cost.CostType;
import com.costflow.accounting.domain.organization.Department;
import com.costflow.accounting.domain.organization.Employee;
import com.costflow.accounting.domain.organization.Headquarters;
import com.costflow.accounting.domain.project.Project;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CostEntryApplicationService {

    private final CostEntryRepository costEntryRepository;

    public CostEntryApplicationService(final CostEntryRepository costEntryRepository) {
        this.costEntryRepository = costEntryRepository;
    }

    @Transactional
    public CostEntry create(
        final CreateCostEntryCommand command,
        final ClosingPeriod closingPeriod,
        final CostCategory costCategory,
        final Project project,
        final Department department,
        final Headquarters headquarters,
        final Employee employee
    ) {
        final CostEntry costEntry = new CostEntry(
            closingPeriod,
            costCategory,
            command.costType(),
            command.costScope(),
            project,
            department,
            headquarters,
            employee,
            command.amount(),
            command.occurredDate(),
            command.description(),
            command.sourceType(),
            command.sourceReference()
        );
        return costEntryRepository.save(costEntry);
    }

    @Transactional
    public CostEntry update(
        final UpdateCostEntryCommand command,
        final CostCategory costCategory,
        final Project project,
        final Department department,
        final Headquarters headquarters,
        final Employee employee
    ) {
        final CostEntry costEntry = costEntryRepository.findById(command.costEntryId())
            .orElseThrow(() -> new NotFoundException("비용을 찾을 수 없습니다. id=" + command.costEntryId()));
        costEntry.update(
            costCategory,
            command.costType(),
            command.costScope(),
            project,
            department,
            headquarters,
            employee,
            command.amount(),
            command.occurredDate(),
            command.description()
        );
        return costEntry;
    }

    @Transactional
    public void delete(final CostEntry costEntry) {
        costEntryRepository.delete(costEntry);
    }

    @Transactional(readOnly = true)
    public CostEntry get(final UUID costEntryId) {
        return costEntryRepository.findById(costEntryId)
            .orElseThrow(() -> new NotFoundException("비용을 찾을 수 없습니다. id=" + costEntryId));
    }

    @Transactional(readOnly = true)
    public List<CostEntry> getByClosingPeriod(final UUID closingPeriodId) {
        return costEntryRepository.findByClosingPeriodId(closingPeriodId);
    }

    @Transactional(readOnly = true)
    public List<CostEntry> getByClosingPeriodAndCostType(final UUID closingPeriodId, final CostType costType) {
        return costEntryRepository.findByClosingPeriodIdAndCostType(closingPeriodId, costType);
    }

    @Transactional(readOnly = true)
    public List<CostEntry> getByClosingPeriodAndCostTypeAndCostScope(
        final UUID closingPeriodId,
        final CostType costType,
        final CostScope costScope
    ) {
        return costEntryRepository.findByClosingPeriodIdAndCostTypeAndCostScope(closingPeriodId, costType, costScope);
    }
}

package com.costflow.accounting.domain.cost;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CostEntryRepository {

    CostEntry save(CostEntry costEntry);

    Optional<CostEntry> findById(UUID id);

    List<CostEntry> findByClosingPeriodId(UUID closingPeriodId);

    void delete(CostEntry costEntry);
}

package com.costflow.accounting.domain.closing;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClosingPeriodRepository {

    ClosingPeriod save(ClosingPeriod closingPeriod);

    Optional<ClosingPeriod> findById(UUID id);

    List<ClosingPeriod> findAll();
}

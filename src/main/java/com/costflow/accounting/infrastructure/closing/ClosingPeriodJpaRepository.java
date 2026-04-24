package com.costflow.accounting.infrastructure.closing;

import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.closing.ClosingPeriodRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClosingPeriodJpaRepository extends JpaRepository<ClosingPeriod, UUID>, ClosingPeriodRepository {
}

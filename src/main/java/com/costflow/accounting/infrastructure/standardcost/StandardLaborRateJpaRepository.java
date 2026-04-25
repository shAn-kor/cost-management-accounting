package com.costflow.accounting.infrastructure.standardcost;

import com.costflow.accounting.domain.standardcost.StandardLaborRate;
import com.costflow.accounting.domain.standardcost.StandardLaborRateRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StandardLaborRateJpaRepository extends JpaRepository<StandardLaborRate, UUID>, StandardLaborRateRepository {

    @Override
    Optional<StandardLaborRate> findFirstByJobGradeIdAndActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByEffectiveFromDesc(
        UUID jobGradeId,
        LocalDate effectiveFrom,
        LocalDate effectiveTo
    );

    @Override
    Optional<StandardLaborRate> findFirstByJobGradeIdAndActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToIsNullOrderByEffectiveFromDesc(
        UUID jobGradeId,
        LocalDate effectiveFrom
    );
}

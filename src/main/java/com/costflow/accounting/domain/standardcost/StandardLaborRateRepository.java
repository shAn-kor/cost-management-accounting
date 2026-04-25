package com.costflow.accounting.domain.standardcost;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StandardLaborRateRepository {

    StandardLaborRate save(StandardLaborRate standardLaborRate);

    Optional<StandardLaborRate> findById(UUID id);

    List<StandardLaborRate> findAll();

    List<StandardLaborRate> findAllByJobGradeIdAndActiveTrue(UUID jobGradeId);

    Optional<StandardLaborRate> findFirstByJobGradeIdAndActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByEffectiveFromDesc(
        UUID jobGradeId,
        LocalDate effectiveFrom,
        LocalDate effectiveTo
    );

    Optional<StandardLaborRate> findFirstByJobGradeIdAndActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToIsNullOrderByEffectiveFromDesc(
        UUID jobGradeId,
        LocalDate effectiveFrom
    );
}

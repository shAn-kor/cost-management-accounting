package com.costflow.accounting.application.standardcost;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.application.common.ValidationException;
import com.costflow.accounting.domain.organization.JobGrade;
import com.costflow.accounting.domain.organization.JobGradeRepository;
import com.costflow.accounting.domain.standardcost.StandardLaborRate;
import com.costflow.accounting.domain.standardcost.StandardLaborRateRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StandardLaborRateApplicationService {

    private final StandardLaborRateRepository standardLaborRateRepository;
    private final JobGradeRepository jobGradeRepository;

    public StandardLaborRateApplicationService(
        final StandardLaborRateRepository standardLaborRateRepository,
        final JobGradeRepository jobGradeRepository
    ) {
        this.standardLaborRateRepository = standardLaborRateRepository;
        this.jobGradeRepository = jobGradeRepository;
    }

    @Transactional
    public StandardLaborRate create(final CreateStandardLaborRateCommand command) {
        final JobGrade jobGrade = jobGradeRepository.findById(command.jobGradeId())
            .orElseThrow(() -> new NotFoundException("직급을 찾을 수 없습니다. id=" + command.jobGradeId()));
        final StandardLaborRate standardLaborRate = new StandardLaborRate(
            jobGrade,
            command.hourlyRate(),
            command.effectiveFrom(),
            command.effectiveTo()
        );
        return standardLaborRateRepository.save(standardLaborRate);
    }

    @Transactional
    public StandardLaborRate update(final UUID standardLaborRateId, final UpdateStandardLaborRateCommand command) {
        final StandardLaborRate standardLaborRate = get(standardLaborRateId);
        standardLaborRate.update(command.hourlyRate(), command.effectiveFrom(), command.effectiveTo());
        return standardLaborRate;
    }

    @Transactional
    public StandardLaborRate changeActive(final UUID standardLaborRateId, final boolean active) {
        final StandardLaborRate standardLaborRate = get(standardLaborRateId);
        standardLaborRate.changeActive(active);
        return standardLaborRate;
    }

    @Transactional(readOnly = true)
    public StandardLaborRate get(final UUID standardLaborRateId) {
        return standardLaborRateRepository.findById(standardLaborRateId)
            .orElseThrow(() -> new NotFoundException("표준단가를 찾을 수 없습니다. id=" + standardLaborRateId));
    }

    @Transactional(readOnly = true)
    public List<StandardLaborRate> getAll() {
        return standardLaborRateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public StandardLaborRate getEffectiveRate(final UUID jobGradeId, final LocalDate effectiveDate) {
        return standardLaborRateRepository
            .findFirstByJobGradeIdAndActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqualOrderByEffectiveFromDesc(
                jobGradeId,
                effectiveDate,
                effectiveDate
            )
            .or(() -> standardLaborRateRepository
                .findFirstByJobGradeIdAndActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToIsNullOrderByEffectiveFromDesc(
                    jobGradeId,
                    effectiveDate
                ))
            .orElseThrow(() -> new ValidationException("유효한 표준단가가 없습니다. jobGradeId=" + jobGradeId));
    }
}

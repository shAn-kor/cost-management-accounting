package com.costflow.accounting.application.organization;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.organization.JobGrade;
import com.costflow.accounting.domain.organization.JobGradeRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobGradeApplicationService {

    private final JobGradeRepository jobGradeRepository;

    public JobGradeApplicationService(final JobGradeRepository jobGradeRepository) {
        this.jobGradeRepository = jobGradeRepository;
    }

    @Transactional
    public JobGrade create(final CreateJobGradeCommand command) {
        return jobGradeRepository.save(new JobGrade(command.code(), command.name(), command.sortOrder()));
    }

    @Transactional
    public JobGrade update(final UUID jobGradeId, final UpdateJobGradeCommand command) {
        final JobGrade jobGrade = get(jobGradeId);
        jobGrade.update(command.name(), command.sortOrder());
        return jobGrade;
    }

    @Transactional
    public JobGrade changeActive(final UUID jobGradeId, final boolean active) {
        final JobGrade jobGrade = get(jobGradeId);
        jobGrade.changeActive(active);
        return jobGrade;
    }

    @Transactional(readOnly = true)
    public JobGrade get(final UUID jobGradeId) {
        return jobGradeRepository.findById(jobGradeId)
            .orElseThrow(() -> new NotFoundException("직급을 찾을 수 없습니다. id=" + jobGradeId));
    }

    @Transactional(readOnly = true)
    public List<JobGrade> getAll() {
        return jobGradeRepository.findAll();
    }
}

package com.costflow.accounting.domain.organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobGradeRepository {

    JobGrade save(JobGrade jobGrade);

    Optional<JobGrade> findById(UUID id);

    List<JobGrade> findAll();
}

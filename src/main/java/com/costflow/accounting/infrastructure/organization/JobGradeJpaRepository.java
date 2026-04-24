package com.costflow.accounting.infrastructure.organization;

import com.costflow.accounting.domain.organization.JobGrade;
import com.costflow.accounting.domain.organization.JobGradeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JobGradeJpaRepository extends JpaRepository<JobGrade, UUID>, JobGradeRepository {
}

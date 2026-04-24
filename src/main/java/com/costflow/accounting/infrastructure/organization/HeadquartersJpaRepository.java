package com.costflow.accounting.infrastructure.organization;

import com.costflow.accounting.domain.organization.Headquarters;
import com.costflow.accounting.domain.organization.HeadquartersRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface HeadquartersJpaRepository extends JpaRepository<Headquarters, UUID>, HeadquartersRepository {
}

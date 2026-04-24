package com.costflow.accounting.domain.organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HeadquartersRepository {

    Headquarters save(Headquarters headquarters);

    Optional<Headquarters> findById(UUID id);

    List<Headquarters> findAll();
}

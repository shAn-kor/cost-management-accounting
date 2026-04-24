package com.costflow.accounting.application.organization;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.organization.Headquarters;
import com.costflow.accounting.domain.organization.HeadquartersRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeadquartersApplicationService {

    private final HeadquartersRepository headquartersRepository;

    public HeadquartersApplicationService(final HeadquartersRepository headquartersRepository) {
        this.headquartersRepository = headquartersRepository;
    }

    @Transactional
    public Headquarters create(final CreateHeadquartersCommand command) {
        return headquartersRepository.save(new Headquarters(command.code(), command.name()));
    }

    @Transactional
    public Headquarters update(final UUID headquartersId, final UpdateHeadquartersCommand command) {
        final Headquarters headquarters = get(headquartersId);
        headquarters.update(command.name());
        return headquarters;
    }

    @Transactional
    public Headquarters changeActive(final UUID headquartersId, final boolean active) {
        final Headquarters headquarters = get(headquartersId);
        headquarters.changeActive(active);
        return headquarters;
    }

    @Transactional(readOnly = true)
    public Headquarters get(final UUID headquartersId) {
        return headquartersRepository.findById(headquartersId)
            .orElseThrow(() -> new NotFoundException("본부를 찾을 수 없습니다. id=" + headquartersId));
    }

    @Transactional(readOnly = true)
    public List<Headquarters> getAll() {
        return headquartersRepository.findAll();
    }
}

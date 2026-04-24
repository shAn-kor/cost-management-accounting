package com.costflow.accounting.application.closing;

import com.costflow.accounting.application.common.NotFoundException;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.closing.ClosingPeriodRepository;
import com.costflow.accounting.domain.closing.ClosingStatePolicy;
import com.costflow.accounting.domain.closing.ClosingStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClosingApplicationService {

    private final ClosingPeriodRepository closingPeriodRepository;
    private final ClosingStatePolicy closingStatePolicy;

    public ClosingApplicationService(final ClosingPeriodRepository closingPeriodRepository, final ClosingStatePolicy closingStatePolicy) {
        this.closingPeriodRepository = closingPeriodRepository;
        this.closingStatePolicy = closingStatePolicy;
    }

    @Transactional
    public ClosingPeriod create(final CreateClosingPeriodCommand command) {
        return closingPeriodRepository.save(new ClosingPeriod(command.yearMonth()));
    }

    @Transactional(readOnly = true)
    public ClosingPeriod get(final UUID closingPeriodId) {
        return closingPeriodRepository.findById(closingPeriodId)
            .orElseThrow(() -> new NotFoundException("마감 월을 찾을 수 없습니다. id=" + closingPeriodId));
    }

    @Transactional(readOnly = true)
    public List<ClosingPeriod> getAll() {
        return closingPeriodRepository.findAll();
    }

    @Transactional(readOnly = true)
    public void validateCostEditable(final UUID closingPeriodId) {
        final ClosingPeriod closingPeriod = closingPeriodRepository.findById(closingPeriodId)
            .orElseThrow(() -> new NotFoundException("마감 월을 찾을 수 없습니다. id=" + closingPeriodId));
        closingStatePolicy.validateCostEditable(closingPeriod.getStatus());
    }

    @Transactional
    public ClosingPeriod transition(final UUID closingPeriodId, final ClosingStatus nextStatus) {
        final ClosingPeriod closingPeriod = closingPeriodRepository.findById(closingPeriodId)
            .orElseThrow(() -> new NotFoundException("마감 월을 찾을 수 없습니다. id=" + closingPeriodId));
        closingPeriod.changeStatus(nextStatus, closingStatePolicy);
        return closingPeriod;
    }
}

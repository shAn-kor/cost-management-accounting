package com.costflow.accounting.infrastructure.transferprice;

import com.costflow.accounting.domain.transferprice.TransferPriceResult;
import com.costflow.accounting.domain.transferprice.TransferPriceResultRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferPriceResultJpaRepository extends JpaRepository<TransferPriceResult, UUID>, TransferPriceResultRepository {

    @Override
    List<TransferPriceResult> findAllByClosingPeriodId(UUID closingPeriodId);

    @Override
    List<TransferPriceResult> findAllByProjectId(UUID projectId);

    @Override
    int countByClosingPeriodId(UUID closingPeriodId);
}

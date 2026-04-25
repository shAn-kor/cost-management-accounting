package com.costflow.accounting.domain.transferprice;

import java.util.List;
import java.util.UUID;

public interface TransferPriceResultRepository {

    TransferPriceResult save(TransferPriceResult transferPriceResult);

    List<TransferPriceResult> findAllByClosingPeriodId(UUID closingPeriodId);

    List<TransferPriceResult> findAllByProjectId(UUID projectId);

    int countByClosingPeriodId(UUID closingPeriodId);
}

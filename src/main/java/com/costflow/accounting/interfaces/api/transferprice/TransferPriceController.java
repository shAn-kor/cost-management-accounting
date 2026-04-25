package com.costflow.accounting.interfaces.api.transferprice;

import com.costflow.accounting.application.facade.TransferPriceCalculationFacade;
import com.costflow.accounting.application.transferprice.TransferPriceApplicationService;
import com.costflow.accounting.domain.transferprice.TransferPriceResult;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TransferPriceController {

    private final TransferPriceCalculationFacade transferPriceCalculationFacade;
    private final TransferPriceApplicationService transferPriceApplicationService;

    public TransferPriceController(
        final TransferPriceCalculationFacade transferPriceCalculationFacade,
        final TransferPriceApplicationService transferPriceApplicationService
    ) {
        this.transferPriceCalculationFacade = transferPriceCalculationFacade;
        this.transferPriceApplicationService = transferPriceApplicationService;
    }

    @PostMapping("/closing-periods/{closingPeriodId}/transfer-prices/calculate")
    @ResponseStatus(HttpStatus.CREATED)
    public List<TransferPriceResultResponse> calculate(@PathVariable final UUID closingPeriodId) {
        return transferPriceCalculationFacade.calculate(closingPeriodId).stream()
            .map(TransferPriceResultResponse::from)
            .toList();
    }

    @GetMapping("/closing-periods/{closingPeriodId}/transfer-prices")
    public List<TransferPriceResultResponse> getByClosingPeriod(@PathVariable final UUID closingPeriodId) {
        return transferPriceApplicationService.getByClosingPeriod(closingPeriodId).stream()
            .map(TransferPriceResultResponse::from)
            .toList();
    }

    @GetMapping("/projects/{projectId}/transfer-prices")
    public List<TransferPriceResultResponse> getByProject(@PathVariable final UUID projectId) {
        return transferPriceApplicationService.getByProject(projectId).stream()
            .map(TransferPriceResultResponse::from)
            .toList();
    }

    public record TransferPriceResultResponse(
        UUID id,
        UUID closingPeriodId,
        UUID projectId,
        UUID employeeId,
        UUID departmentId,
        UUID standardLaborRateId,
        BigDecimal workHours,
        BigDecimal hourlyRate,
        BigDecimal transferAmount,
        int calculationVersion
    ) {
        public static TransferPriceResultResponse from(final TransferPriceResult result) {
            return new TransferPriceResultResponse(
                result.getId(),
                result.getClosingPeriod().getId(),
                result.getProject().getId(),
                result.getEmployee().getId(),
                result.getDepartment().getId(),
                result.getStandardLaborRate().getId(),
                result.getWorkHours(),
                result.getHourlyRate(),
                result.getTransferAmount(),
                result.getCalculationVersion()
            );
        }
    }
}

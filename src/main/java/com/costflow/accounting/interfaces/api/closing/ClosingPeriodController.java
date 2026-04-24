package com.costflow.accounting.interfaces.api.closing;

import com.costflow.accounting.application.closing.ClosingApplicationService;
import com.costflow.accounting.application.closing.CreateClosingPeriodCommand;
import com.costflow.accounting.domain.closing.ClosingPeriod;
import com.costflow.accounting.domain.closing.ClosingStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/closing-periods")
public class ClosingPeriodController {

    private final ClosingApplicationService closingApplicationService;

    public ClosingPeriodController(final ClosingApplicationService closingApplicationService) {
        this.closingApplicationService = closingApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClosingPeriodResponse create(@Valid @RequestBody final CreateClosingPeriodRequest request) {
        final ClosingPeriod closingPeriod = closingApplicationService.create(new CreateClosingPeriodCommand(request.yearMonth()));
        return ClosingPeriodResponse.from(closingPeriod);
    }

    @GetMapping("/{closingPeriodId}")
    public ClosingPeriodResponse get(@PathVariable final UUID closingPeriodId) {
        return ClosingPeriodResponse.from(closingApplicationService.get(closingPeriodId));
    }

    @GetMapping
    public List<ClosingPeriodResponse> getAll() {
        return closingApplicationService.getAll().stream()
            .map(ClosingPeriodResponse::from)
            .toList();
    }

    @PatchMapping("/{closingPeriodId}/status")
    public ClosingPeriodResponse transition(@PathVariable final UUID closingPeriodId, @Valid @RequestBody final TransitionClosingPeriodRequest request) {
        return ClosingPeriodResponse.from(closingApplicationService.transition(closingPeriodId, request.nextStatus()));
    }

    public record CreateClosingPeriodRequest(
        @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}") String yearMonth
    ) {
    }

    public record TransitionClosingPeriodRequest(
        @NotNull ClosingStatus nextStatus
    ) {
    }

    public record ClosingPeriodResponse(
        UUID id,
        String yearMonth,
        ClosingStatus status,
        long version
    ) {
        public static ClosingPeriodResponse from(final ClosingPeriod closingPeriod) {
            return new ClosingPeriodResponse(
                closingPeriod.getId(),
                closingPeriod.getYearMonth(),
                closingPeriod.getStatus(),
                closingPeriod.getVersion()
            );
        }
    }
}

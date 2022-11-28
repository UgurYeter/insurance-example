package yeter.ugur.insuranceexample.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PolicyModificationResponseDto {

    private String policyId;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate effectiveDate;

    private List<InsuredPersonDto> insuredPersons;

    private BigDecimal totalPremium;
}

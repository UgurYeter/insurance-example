package yeter.ugur.insuranceexample.api;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PolicyResponse {

    private String policyId;

    private LocalDate effectiveDate;

    private List<InsuredPerson> insuredPersons;

    private BigDecimal totalPremium;
}

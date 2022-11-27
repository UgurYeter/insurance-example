package yeter.ugur.insuranceexample.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PolicyCreationResponse {

    private String policyId;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    private List<InsuredPerson> insuredPersons;

    private BigDecimal totalPremium;
}

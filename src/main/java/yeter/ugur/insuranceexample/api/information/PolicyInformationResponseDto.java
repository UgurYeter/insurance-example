package yeter.ugur.insuranceexample.api.information;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PolicyInformationResponseDto {

    private String policyId;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate requestDate;

    private List<InsuredPersonDto> insuredPersons;

    private BigDecimal totalPremium;
}

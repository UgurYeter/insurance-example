package yeter.ugur.insuranceexample.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolicyResponseDto {

    private String policyId;

    private List<InsuredPersonDto> insuredPersons;

    private BigDecimal totalPremium;
}

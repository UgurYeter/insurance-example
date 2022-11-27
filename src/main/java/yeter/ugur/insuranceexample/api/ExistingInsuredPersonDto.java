package yeter.ugur.insuranceexample.api;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ExistingInsuredPersonDto {

    private Integer id;

    private String firstName;

    private String secondName;

    private BigDecimal premium;
}

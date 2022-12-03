package yeter.ugur.insuranceexample.api.modification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static yeter.ugur.insuranceexample.AppConfig.DATE_FORMAT;

@Data
@NoArgsConstructor
public class PolicyModificationRequestDto {

    private String policyId;

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDate effectiveDate;

    private List<InsuredPersonDto> insuredPersons = new ArrayList<>();

}

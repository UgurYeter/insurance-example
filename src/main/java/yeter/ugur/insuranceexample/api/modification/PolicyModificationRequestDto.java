package yeter.ugur.insuranceexample.api.modification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PolicyModificationRequestDto {

    @NonNull
    private String policyId;

    @NonNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate effectiveDate;

    private List<InsuredPersonDto> insuredPersons = new ArrayList<>();

}

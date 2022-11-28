package yeter.ugur.insuranceexample.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PolicyModificationRequestDto {

    @NonNull
    private String policyId;

    @NonNull
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate effectiveDate;

    private List<InsuredPersonDto> insuredPersons = new ArrayList<>();

}
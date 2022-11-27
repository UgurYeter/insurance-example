package yeter.ugur.insuranceexample.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PolicyCreationRequestDto {

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    private List<InsuredPerson> insuredPersons;

}

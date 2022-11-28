package yeter.ugur.insuranceexample.api.creation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PolicyCreationRequestDto {

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    private List<InsuredPersonDto> insuredPersons = new ArrayList<>();

}

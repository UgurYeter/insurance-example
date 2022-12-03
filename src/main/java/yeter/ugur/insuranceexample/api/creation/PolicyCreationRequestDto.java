package yeter.ugur.insuranceexample.api.creation;

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
public class PolicyCreationRequestDto {

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDate startDate;

    private List<InsuredPersonDto> insuredPersons = new ArrayList<>();

}

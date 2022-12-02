package yeter.ugur.insuranceexample.api.information;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import yeter.ugur.insuranceexample.api.PolicyResponseDto;

import java.time.LocalDate;

@Data
public class PolicyInformationResponseDto extends PolicyResponseDto {

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate requestDate;
}

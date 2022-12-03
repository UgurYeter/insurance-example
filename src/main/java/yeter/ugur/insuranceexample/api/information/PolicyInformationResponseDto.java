package yeter.ugur.insuranceexample.api.information;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import yeter.ugur.insuranceexample.api.PolicyResponseDto;

import java.time.LocalDate;

import static yeter.ugur.insuranceexample.AppConfig.DATE_FORMAT;

@Data
public class PolicyInformationResponseDto extends PolicyResponseDto {

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDate requestDate;
}

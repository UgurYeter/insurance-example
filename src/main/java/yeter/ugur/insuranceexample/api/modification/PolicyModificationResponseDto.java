package yeter.ugur.insuranceexample.api.modification;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import yeter.ugur.insuranceexample.api.PolicyResponseDto;

import java.time.LocalDate;

import static yeter.ugur.insuranceexample.AppConfig.DATE_FORMAT;

@Data
public class PolicyModificationResponseDto extends PolicyResponseDto {

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDate effectiveDate;
}

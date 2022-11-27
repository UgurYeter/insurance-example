package yeter.ugur.insuranceexample.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@Builder
public class InsuredPerson {

    @JsonIgnore
    @JsonProperty(access = WRITE_ONLY)
    private Integer id;

    private String firstName;

    private String secondName;

    private BigDecimal premium;
}

package yeter.ugur.insuranceexample;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {

    private String message;
    private long timestamp;
}

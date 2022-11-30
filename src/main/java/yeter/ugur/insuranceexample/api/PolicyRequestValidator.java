package yeter.ugur.insuranceexample.api;

import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyDateException;

import java.time.Clock;
import java.time.LocalDate;

@Component
public class PolicyRequestValidator {

    private final Clock clock;

    public PolicyRequestValidator(Clock clock) {
        this.clock = clock;
    }

    public void verifyCreatePolicyOrThrow(PolicyCreationRequestDto policyCreationRequestDto) {
        LocalDate startDate = policyCreationRequestDto.getStartDate();
        if (startDate == null) {
            throw new PolicyDateException("Policy start date can not be null!");
        }
        if (!startDate.isAfter(LocalDate.now(clock))) {
            throw new PolicyDateException("Policy start date can only be in future!");
        }
    }
}

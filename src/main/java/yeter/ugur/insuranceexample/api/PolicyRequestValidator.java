package yeter.ugur.insuranceexample.api;

import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyDateException;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.service.helper.TimeHelper;

import java.time.LocalDate;

@Component
public class PolicyRequestValidator {

    private final TimeHelper timeHelper;

    public PolicyRequestValidator(TimeHelper timeHelper) {
        this.timeHelper = timeHelper;
    }

    public void verifyCreatePolicyOrThrow(PolicyCreationRequestDto policyCreationRequestDto) {
        LocalDate startDate = policyCreationRequestDto.getStartDate();
        verifyItIsNotNullOrThrow(startDate, "Policy start date can not be null!");
        verifyFutureDateOrThrow(startDate, "Policy start date can only be in future!");
    }

    private static void verifyItIsNotNullOrThrow(LocalDate startDate, String message) {
        if (startDate == null) {
            throw new PolicyDateException(message);
        }
    }

    private void verifyFutureDateOrThrow(LocalDate startDate, String message) {
        if (!startDate.isAfter(timeHelper.getLocalDateNow())) {
            throw new PolicyDateException(message);
        }
    }

    public void verifyModificationPolicyOrThrow(PolicyModificationRequestDto policyModificationRequestDto) {
        LocalDate effectiveDate = policyModificationRequestDto.getEffectiveDate();
        verifyItIsNotNullOrThrow(effectiveDate, "Policy effectiveDate date can not be null!");
        verifyFutureDateOrThrow(effectiveDate, "Policy effectiveDate date can only be in future!");
    }

}

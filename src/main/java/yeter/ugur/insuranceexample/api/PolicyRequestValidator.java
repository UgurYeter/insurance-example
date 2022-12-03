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
        if (startDate == null) {
            throw new PolicyDateException("Policy start date can not be null!");
        }
        if (!startDate.isAfter(timeHelper.getLocalDateNow())) {
            throw new PolicyDateException("Policy start date can only be in future!");
        }
    }

    public void verifyModificationPolicyOrThrow(PolicyModificationRequestDto policyModificationRequestDto) {
        LocalDate effectiveDate = policyModificationRequestDto.getEffectiveDate();
        if (effectiveDate == null) {
            throw new PolicyDateException("Policy effectiveDate date can not be null!");
        }
        if (!effectiveDate.isAfter(LocalDate.now())) {
            throw new PolicyDateException("Policy effectiveDate date can only be in future!");
        }
    }

}

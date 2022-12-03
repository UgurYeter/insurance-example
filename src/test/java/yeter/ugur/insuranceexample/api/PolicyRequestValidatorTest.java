package yeter.ugur.insuranceexample.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyDateException;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.helper.PolicyTestDataHelper;
import yeter.ugur.insuranceexample.service.helper.TimeHelper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.START_DATE_1;

@ExtendWith(MockitoExtension.class)
class PolicyRequestValidatorTest {

    @Mock
    private TimeHelper timeHelper;

    @InjectMocks
    private PolicyRequestValidator policyRequestValidator;

    @Test
    void itThrowsExceptionWhenStartDateIsNull() {
        PolicyCreationRequestDto policyCreationRequestDto = PolicyTestDataHelper.prototypeRequestWithoutInsuredPerson();
        policyCreationRequestDto.setStartDate(null);

        assertThrows(PolicyDateException.class, () -> policyRequestValidator.verifyCreatePolicyOrThrow(policyCreationRequestDto));
    }


    @Test
    void creationRequestValidationThrowsExceptionWhenStartDateIsNotFutureDate() {
        when(timeHelper.getLocalDateNow()).thenReturn(START_DATE_1);
        PolicyCreationRequestDto policyCreationRequestDto = PolicyTestDataHelper.prototypeRequestWithoutInsuredPerson();
        policyCreationRequestDto.setStartDate(START_DATE_1.minusDays(1));

        assertThrows(PolicyDateException.class, () -> policyRequestValidator.verifyCreatePolicyOrThrow(policyCreationRequestDto));
    }

    @Test
    void itVerifiesCreationRequestSuccessfullyWhenChecksPass() {
        when(timeHelper.getLocalDateNow()).thenReturn(START_DATE_1);
        PolicyCreationRequestDto policyCreationRequestDto = PolicyTestDataHelper.prototypeRequestWithoutInsuredPerson();
        policyCreationRequestDto.setStartDate(START_DATE_1.plusDays(1));

        policyRequestValidator.verifyCreatePolicyOrThrow(policyCreationRequestDto);
    }

    @Test
    void modificationRequestValidationThrowsExceptionWhenEffectiveDateIsNull() {
        PolicyModificationRequestDto modificationRequestDto = PolicyTestDataHelper.prototypeModificationRequestionWithoutInsuredPersons();
        modificationRequestDto.setEffectiveDate(null);

        assertThrows(PolicyDateException.class, () -> policyRequestValidator.verifyModificationPolicyOrThrow(modificationRequestDto));
    }

    @Test
    void modificationRequestValidationThrowsExceptionWhenEffectiveDateIsNotFutureDate() {
        when(timeHelper.getLocalDateNow()).thenReturn(START_DATE_1);
        PolicyModificationRequestDto modificationRequestDto = PolicyTestDataHelper.prototypeModificationRequestionWithoutInsuredPersons();
        modificationRequestDto.setEffectiveDate(START_DATE_1.minusDays(1));

        assertThrows(PolicyDateException.class, () -> policyRequestValidator.verifyModificationPolicyOrThrow(modificationRequestDto));
    }
}

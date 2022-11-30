package yeter.ugur.insuranceexample.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.helper.PolicyTestDataHelper;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyDateException;
import yeter.ugur.insuranceexample.service.PolicyCreationService;
import yeter.ugur.insuranceexample.service.PolicyInformationService;
import yeter.ugur.insuranceexample.service.PolicyModificationService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class PolicyControllerTest {

    @Mock
    private PolicyCreationService policyCreationService;
    @Mock
    private PolicyModificationService policyModificationService;
    @Mock
    private PolicyInformationService policyInformationService;
    @Mock
    private PolicyRequestValidator policyRequestValidator;
    @InjectMocks
    private PolicyController policyController;

    @Test
    void itVerifiesPolicyCreationRequestDto() {
        PolicyCreationRequestDto policyCreationRequestDto = PolicyTestDataHelper.prototypeRequestWithInsuredPersons();
        doThrow(new PolicyDateException("Verification Failed!"))
                .when(policyRequestValidator)
                .verifyCreatePolicyOrThrow(policyCreationRequestDto);

        assertThrows(PolicyDateException.class, () ->
                policyController.createPolicy(policyCreationRequestDto));

        verify(policyRequestValidator).verifyCreatePolicyOrThrow(policyCreationRequestDto);
        verifyNoInteractions(policyCreationService);
    }

    @Test
    void itStartsPolicyCreationWhenRequestValid() {
        PolicyCreationRequestDto policyCreationRequestDto = PolicyTestDataHelper.prototypeRequestWithInsuredPersons();
        doNothing()
                .when(policyRequestValidator)
                .verifyCreatePolicyOrThrow(policyCreationRequestDto);

        policyController.createPolicy(policyCreationRequestDto);

        verify(policyRequestValidator).verifyCreatePolicyOrThrow(policyCreationRequestDto);
        verify(policyCreationService).createPolicy(policyCreationRequestDto);
    }
}

package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.api.information.PolicyInformationResponseDto;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.helper.InsuredPersonTestHelper;
import yeter.ugur.insuranceexample.helper.PolicyTestDataHelper;
import yeter.ugur.insuranceexample.helper.TestMockDataHelper;
import yeter.ugur.insuranceexample.service.helper.PolicyPremiumHelper;
import yeter.ugur.insuranceexample.service.helper.PolicyStateHelper;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.EXTERNAL_POLICY_ID;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.START_DATE_1;

@ExtendWith(MockitoExtension.class)
class PolicyInformationServiceTest {

    @Mock
    private PolicyStateHelper policyStateHelper;
    @Mock
    private InsuredPersonMapper insuredPersonMapper;
    @Mock
    private PolicyPremiumHelper policyPremiumHelper;
    @InjectMocks
    private PolicyInformationService policyInformationService;

    @Test
    void itThrowsExceptionWhenThereIsNoPolicyState() {
        when(policyStateHelper.findLatestPolicyStatePriorToDate(EXTERNAL_POLICY_ID, START_DATE_1))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> policyInformationService
                .getPolicyInformation(EXTERNAL_POLICY_ID, START_DATE_1));
    }

    @Test
    void itReturnsPolicyInformation() {
        PolicyEntity policyEntity = PolicyTestDataHelper.prototypePolicyEntity().build();
        when(policyStateHelper.findLatestPolicyStatePriorToDate(policyEntity.getExternalId(),
                policyEntity.getStartDate()))
                .thenReturn(Optional.of(policyEntity));
        when(policyPremiumHelper.calculateTotalPremium(policyEntity.getInsuredPersons())).thenReturn(TestMockDataHelper.PREMIUM_2);
        List<InsuredPersonDto> insuredPersonDtos = InsuredPersonTestHelper.prototypeInsuredPersonDto();
        when(insuredPersonMapper.toInsuredPersonsDto(policyEntity.getInsuredPersons()))
                .thenReturn(insuredPersonDtos);

        PolicyInformationResponseDto result = policyInformationService
                .getPolicyInformation(policyEntity.getExternalId(), policyEntity.getStartDate());

        assertThat(result.getPolicyId()).isEqualTo(policyEntity.getExternalId());
        assertThat(result.getRequestDate()).isEqualTo(policyEntity.getStartDate());
        assertThat(result.getTotalPremium()).isEqualTo(TestMockDataHelper.PREMIUM_2);
        assertThat(result.getInsuredPersons()).isEqualTo(insuredPersonDtos);
    }
}

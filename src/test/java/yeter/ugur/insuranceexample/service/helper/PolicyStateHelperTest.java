package yeter.ugur.insuranceexample.service.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;
import yeter.ugur.insuranceexample.helper.PolicyTestDataHelper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.EXTERNAL_POLICY_ID;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.POLICY_ID_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.POLICY_ID_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.START_DATE_1;

@ExtendWith(MockitoExtension.class)
class PolicyStateHelperTest {

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PolicyStateHelper policyStateHelper;

    @Test
    void itReturnsEmptyWhenNoPolicyFound() {
        when(policyRepository.findByExternalId(EXTERNAL_POLICY_ID))
                .thenReturn(List.of());

        Optional<PolicyEntity> found = policyStateHelper
                .findLatestPolicyStatePriorOrEqualToDate(EXTERNAL_POLICY_ID, START_DATE_1);

        assertThat(found).isEmpty();
    }

    @Test
    void itReturnsEmptyWhenFoundPoliciesEffectiveAfterStartDate() {
        when(policyRepository.findByExternalId(EXTERNAL_POLICY_ID))
                .thenReturn(List.of(
                        PolicyTestDataHelper.prototypePolicyEntity()
                                .id(POLICY_ID_1)
                                .startDate(START_DATE_1.plusDays(1))
                                .build(),
                        PolicyTestDataHelper.prototypePolicyEntity()
                                .id(POLICY_ID_2)
                                .startDate(START_DATE_1.plusDays(2))
                                .build()
                ));

        Optional<PolicyEntity> found = policyStateHelper
                .findLatestPolicyStatePriorOrEqualToDate(EXTERNAL_POLICY_ID, START_DATE_1);

        assertThat(found).isEmpty();
    }

    @Test
    void itReturnsLatestPolicyStatePriorToStartDate() {
        PolicyEntity latestPriorPolicy = PolicyTestDataHelper.prototypePolicyEntity()
                .id(POLICY_ID_2)
                .startDate(START_DATE_1.minusDays(1))
                .build();
        when(policyRepository.findByExternalId(EXTERNAL_POLICY_ID))
                .thenReturn(List.of(
                        PolicyTestDataHelper.prototypePolicyEntity()
                                .id(POLICY_ID_1)
                                .startDate(START_DATE_1.minusDays(2))
                                .build(),
                        latestPriorPolicy
                ));

        Optional<PolicyEntity> found = policyStateHelper
                .findLatestPolicyStatePriorOrEqualToDate(EXTERNAL_POLICY_ID, START_DATE_1);

        assertThat(found).contains(latestPriorPolicy);
    }

    @Test
    void itReturnsPolicyWithTheSameStartDate() {
        PolicyEntity latestPolicyWithSameStartDate = PolicyTestDataHelper.prototypePolicyEntity()
                .id(POLICY_ID_2)
                .startDate(START_DATE_1)
                .build();
        when(policyRepository.findByExternalId(EXTERNAL_POLICY_ID))
                .thenReturn(List.of(
                        PolicyTestDataHelper.prototypePolicyEntity()
                                .id(POLICY_ID_1)
                                .startDate(START_DATE_1.minusDays(2))
                                .build(),
                        PolicyTestDataHelper.prototypePolicyEntity()
                                .id(POLICY_ID_2)
                                .startDate(START_DATE_1.minusDays(1))
                                .build(),
                        latestPolicyWithSameStartDate
                ));

        Optional<PolicyEntity> found = policyStateHelper
                .findLatestPolicyStatePriorOrEqualToDate(EXTERNAL_POLICY_ID, START_DATE_1);

        assertThat(found).contains(latestPolicyWithSameStartDate);
    }

}

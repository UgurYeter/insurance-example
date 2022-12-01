package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;
import yeter.ugur.insuranceexample.helper.InsuredPersonEntityTestHelper;
import yeter.ugur.insuranceexample.helper.PolicyTestDataHelper;
import yeter.ugur.insuranceexample.helper.TestMockDataHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyAndInsuredPersonStorageHelperTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private InsuredPersonRepository insuredPersonRepository;
    @InjectMocks
    private PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper;

    @Test
    void itCreatesPolicyWithInsuredPersons() {
        PolicyEntity policyWithoutPersons = PolicyTestDataHelper.prototypePolicyEntityWithoutId().build();
        List<InsuredPersonEntity> insuredPersons = InsuredPersonEntityTestHelper.getInsuredPersonEntities();
        PolicyEntity savedPolicy = PolicyTestDataHelper.prototypePolicyEntityWithoutId()
                .id(TestMockDataHelper.POLICY_ID_1)
                .insuredPersons(insuredPersons)
                .build();
        when(policyRepository.save(policyWithoutPersons)).thenReturn(savedPolicy);

        PolicyEntity returnedPolicyEntity = policyAndInsuredPersonStorageHelper.createPolicyWithInsuredPersons(
                policyWithoutPersons,
                insuredPersons);

        verify(policyRepository).save(policyWithoutPersons);
        verify(insuredPersonRepository).saveAll(insuredPersons);
        assertThat(returnedPolicyEntity).isEqualTo(savedPolicy);
        verifyNoMoreInteractions(policyRepository, insuredPersonRepository);
    }

    @Test
    void itCreatesPolicyWithoutInsuredPersons() {
        PolicyEntity policyEntity = PolicyTestDataHelper.prototypePolicyEntityWithoutId().build();
        List<InsuredPersonEntity> insuredPersonEntities = List.of();
        PolicyEntity savedPolicy = PolicyTestDataHelper.prototypePolicyEntityWithoutId()
                .id(TestMockDataHelper.POLICY_ID_1)
                .build();
        when(policyRepository.save(policyEntity)).thenReturn(savedPolicy);

        PolicyEntity returnedPolicyEntity = policyAndInsuredPersonStorageHelper.createPolicyWithInsuredPersons(
                policyEntity,
                insuredPersonEntities);

        verify(policyRepository).save(policyEntity);
        assertThat(returnedPolicyEntity).isEqualTo(savedPolicy);
        verifyNoInteractions(insuredPersonRepository);
        verifyNoMoreInteractions(policyRepository);
    }
}

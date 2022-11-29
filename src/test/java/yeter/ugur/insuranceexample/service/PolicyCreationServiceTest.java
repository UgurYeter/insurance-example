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

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static yeter.ugur.insuranceexample.TestHelper.EXTERNAL_POLICY_ID;
import static yeter.ugur.insuranceexample.TestHelper.FIRST_NAME_1;
import static yeter.ugur.insuranceexample.TestHelper.FIRST_NAME_2;
import static yeter.ugur.insuranceexample.TestHelper.NOW_IN_MILLI;
import static yeter.ugur.insuranceexample.TestHelper.PREMIUM_1;
import static yeter.ugur.insuranceexample.TestHelper.PREMIUM_2;
import static yeter.ugur.insuranceexample.TestHelper.SECOND_NAME_1;
import static yeter.ugur.insuranceexample.TestHelper.SECOND_NAME_2;
import static yeter.ugur.insuranceexample.TestHelper.START_DATE;

@ExtendWith(MockitoExtension.class)
class PolicyCreationServiceTest {

    @Mock
    private ExternalPolicyIdGenerator externalPolicyIdGenerator;

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private InsuredPersonRepository insuredPersonRepository;

    @InjectMocks
    private PolicyCreationService policyCreationService;

    @Test
    void itChecksIfPolicyIdUnique() {
        when(externalPolicyIdGenerator.generate()).thenReturn(EXTERNAL_POLICY_ID);
        when(policyRepository.findByExternalId(EXTERNAL_POLICY_ID)).thenReturn(List.of());

        String uniqueExternalPolicyId = policyCreationService.generateUniquePolicyId();

        verify(policyRepository).findByExternalId(EXTERNAL_POLICY_ID);
        assertThat(uniqueExternalPolicyId).isEqualTo(EXTERNAL_POLICY_ID);
        verifyNoMoreInteractions(policyRepository);
    }


//    @Test
//    void itSavesPolicyOnly() {
//        PolicyEntity policyEntity = PolicyEntity.builder()
//                .externalId(EXTERNAL_POLICY_ID)
//                .startDate(START_DATE)
//                .createdAt(NOW_IN_MILLI)
//                .build();
//        when(policyRepository.save(policyEntity)).thenReturn(policyEntity);
//
//        PolicyEntity storedPolicy = policyCreationService.createPolicyWithInsuredPersons(
//                policyEntity, List.of());
//
//        verify(policyRepository).save(policyEntity);
//        assertThat(storedPolicy.getExternalId()).isEqualTo(EXTERNAL_POLICY_ID);
//        assertThat(storedPolicy.getStartDate()).isEqualTo(START_DATE);
//        assertThat(storedPolicy.getCreatedAt()).isEqualTo(NOW_IN_MILLI);
//        assertThat(storedPolicy.getInsuredPersons()).isEmpty();
//        verifyNoMoreInteractions(policyRepository);
//    }

//    @Test
//    void itSavesPolicyAndInsuredPersons() {
//        PolicyEntity policyEntity = PolicyEntity.builder()
//                .externalId(EXTERNAL_POLICY_ID)
//                .startDate(START_DATE)
//                .createdAt(NOW_IN_MILLI)
//                .build();
//        when(policyRepository.save(policyEntity)).thenReturn(policyEntity);
//        when(insuredPersonRepository.saveAll(any()))
//                .thenAnswer(methodCall -> methodCall.getArguments()[0]);
//        List<InsuredPersonEntity> insuredPersons = List.of(InsuredPersonEntity.builder()
//                        .firstName(FIRST_NAME_1)
//                        .secondName(SECOND_NAME_1)
//                        .premium(PREMIUM_1)
//                        .build(),
//                InsuredPersonEntity.builder()
//                        .firstName(FIRST_NAME_2)
//                        .secondName(SECOND_NAME_2)
//                        .premium(PREMIUM_2)
//                        .build());
//
//        PolicyEntity storedPolicy = policyCreationService.createPolicyWithInsuredPersons(
//                policyEntity,
//                insuredPersons);
//
//        verify(insuredPersonRepository).saveAll(insuredPersons);
//        assertThat(storedPolicy.getInsuredPersons()).isEqualTo(insuredPersons);
//        verify(policyRepository).save(policyEntity);
//        verifyNoMoreInteractions(policyRepository, insuredPersonRepository);
//
//    }
}

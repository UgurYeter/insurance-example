package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.helper.PolicyCreationRequestDtoTestHelper;
import yeter.ugur.insuranceexample.service.mapper.PolicyObjectsMapper;

@ExtendWith(MockitoExtension.class)
class PolicyCreationServiceTest {

    @Mock
    private PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper;

    @Mock
    private PolicyObjectsMapper policyObjectsMapper;

    @InjectMocks
    private PolicyCreationService policyCreationService;

    @Test
    void createPolicy() {

//        policyCreationService.createPolicy(PolicyCreationRequestDtoTestHelper.prototypeRequestWithInsuredPersons());
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

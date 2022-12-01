package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.helper.InsuredPersonEntityTestHelper;
import yeter.ugur.insuranceexample.helper.PolicyTestDataHelper;
import yeter.ugur.insuranceexample.service.helper.ExternalPolicyIdProvider;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;
import yeter.ugur.insuranceexample.service.mapper.PolicyObjectsMapper;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.EXTERNAL_POLICY_ID;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.NOW_IN_MILLI;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.START_DATE;

@ExtendWith(MockitoExtension.class)
class PolicyCreationServiceTest {

    @Mock
    private PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper;

    @Mock
    private PolicyObjectsMapper policyObjectsMapper;

    @Mock
    private InsuredPersonMapper insuredPersonMapper;
    @Mock
    private ExternalPolicyIdProvider externalPolicyIdProvider;

    @InjectMocks
    private PolicyCreationService policyCreationService;

    @Test
    void createPolicy() {
        when(externalPolicyIdProvider.generateExternalPolicyId()).thenReturn(EXTERNAL_POLICY_ID);
        PolicyCreationRequestDto creationRequestDto = PolicyTestDataHelper
                .prototypeRequestWithInsuredPersons();
        PolicyEntity policyEntity = PolicyEntity.builder()
                .externalId(EXTERNAL_POLICY_ID)
                .startDate(START_DATE)
                .createdAt(NOW_IN_MILLI)
                .build();
        when(policyObjectsMapper.mapToPolicyEntityWithoutInsuredPersons(creationRequestDto, EXTERNAL_POLICY_ID))
                .thenReturn(policyEntity);
        List<InsuredPersonEntity> insuredPersonEntities = InsuredPersonEntityTestHelper.getInsuredPersonEntities();
        when(insuredPersonMapper.toInsuredPersonEntities(creationRequestDto.getInsuredPersons()))
                .thenReturn(insuredPersonEntities);
        policyEntity.addPersons(insuredPersonEntities);
        when(policyAndInsuredPersonStorageHelper.createPolicyWithInsuredPersons(policyEntity,
                insuredPersonEntities))
                .thenReturn(policyEntity);

        policyCreationService.createPolicy(creationRequestDto);

        verify(policyObjectsMapper).mapToPolicyEntityWithoutInsuredPersons(creationRequestDto, EXTERNAL_POLICY_ID);
        verify(policyObjectsMapper).toPolicyCreationResponseDto(
                policyEntity.getInsuredPersons(),
                policyEntity.getExternalId(),
                policyEntity.getStartDate());
    }
}

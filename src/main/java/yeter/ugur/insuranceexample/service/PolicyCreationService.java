package yeter.ugur.insuranceexample.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.helper.ExternalPolicyIdProvider;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;
import yeter.ugur.insuranceexample.service.mapper.PolicyObjectsMapper;

import java.util.List;

@Slf4j
@Service
public class PolicyCreationService {

    private final PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper;
    private final PolicyObjectsMapper policyObjectsMapper;
    private final InsuredPersonMapper insuredPersonMapper;
    private final ExternalPolicyIdProvider externalPolicyIdProvider;

    public PolicyCreationService(
            PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper,
            PolicyObjectsMapper policyObjectsMapper,
            InsuredPersonMapper insuredPersonMapper,
            ExternalPolicyIdProvider externalPolicyIdProvider) {
        this.policyAndInsuredPersonStorageHelper = policyAndInsuredPersonStorageHelper;
        this.policyObjectsMapper = policyObjectsMapper;
        this.insuredPersonMapper = insuredPersonMapper;
        this.externalPolicyIdProvider = externalPolicyIdProvider;
    }

    @Transactional
    public PolicyCreationResponseDto createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = externalPolicyIdProvider.generateExternalPolicyId();
        PolicyEntity policyEntity = policyObjectsMapper.mapToPolicyEntityWithoutInsuredPersons(externalPolicyId, creationRequestDto.getStartDate());
        List<InsuredPersonEntity> insuredPersons = insuredPersonMapper.toInsuredPersonEntities(creationRequestDto.getInsuredPersons());
        PolicyEntity storedPolicy = policyAndInsuredPersonStorageHelper.createPolicyWithInsuredPersons(policyEntity, insuredPersons);
        return policyObjectsMapper.
                toPolicyCreationResponseDto(
                        storedPolicy.getInsuredPersons(),
                        storedPolicy.getExternalId(),
                        storedPolicy.getStartDate());
    }
}

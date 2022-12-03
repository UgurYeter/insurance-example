package yeter.ugur.insuranceexample.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.helper.ExternalPolicyIdProvider;
import yeter.ugur.insuranceexample.service.helper.PolicyStateHelper;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;
import yeter.ugur.insuranceexample.service.mapper.PolicyObjectsMapper;

import java.util.List;

@Slf4j
@Service
public class PolicyCreationService {

    private final PolicyStateHelper policyStateHelper;
    private final PolicyObjectsMapper policyObjectsMapper;
    private final InsuredPersonMapper insuredPersonMapper;
    private final ExternalPolicyIdProvider externalPolicyIdProvider;

    public PolicyCreationService(
            PolicyStateHelper policyStateHelper, PolicyObjectsMapper policyObjectsMapper,
            InsuredPersonMapper insuredPersonMapper,
            ExternalPolicyIdProvider externalPolicyIdProvider) {
        this.policyStateHelper = policyStateHelper;
        this.policyObjectsMapper = policyObjectsMapper;
        this.insuredPersonMapper = insuredPersonMapper;
        this.externalPolicyIdProvider = externalPolicyIdProvider;
    }

    public PolicyCreationResponseDto createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = externalPolicyIdProvider.generateExternalPolicyId();
        PolicyEntity policyEntity = policyObjectsMapper.mapToPolicyEntityWithoutInsuredPersons(externalPolicyId, creationRequestDto.getStartDate());
        List<InsuredPersonEntity> insuredPersons = insuredPersonMapper.toInsuredPersonEntities(creationRequestDto.getInsuredPersons());
        PolicyEntity storedPolicy = policyStateHelper.createNewPolicyState(policyEntity, insuredPersons);
        PolicyCreationResponseDto policyCreationResponseDto = policyObjectsMapper.
                toPolicyCreationResponseDto(
                        storedPolicy.getInsuredPersons(),
                        storedPolicy.getExternalId(),
                        storedPolicy.getStartDate());
        log.info("Policy with id:{} is successfully created!", externalPolicyId);
        return policyCreationResponseDto;
    }
}

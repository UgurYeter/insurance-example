package yeter.ugur.insuranceexample.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.mapper.PolicyObjectsMapper;

import java.util.List;
import java.util.UUID;

import static yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper.toInsuredPersonEntities;

@Slf4j
@Service
public class PolicyCreationService {

    private final PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper;
    private final PolicyObjectsMapper policyObjectsMapper;

    public PolicyCreationService(
            PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper,
            PolicyObjectsMapper policyObjectsMapper) {
        this.policyAndInsuredPersonStorageHelper = policyAndInsuredPersonStorageHelper;
        this.policyObjectsMapper = policyObjectsMapper;
    }

    @Transactional
    public PolicyCreationResponseDto createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = UUID.randomUUID().toString();
        PolicyEntity policyEntity = policyObjectsMapper.mapToPolicyEntityWithoutInsuredPersons(creationRequestDto, externalPolicyId);
        List<InsuredPersonEntity> insuredPersons = toInsuredPersonEntities(creationRequestDto.getInsuredPersons());
        PolicyEntity storedPolicy = policyAndInsuredPersonStorageHelper.createPolicyWithInsuredPersons(policyEntity, insuredPersons);
        return policyObjectsMapper.
                toPolicyCreationResponseDto(
                        storedPolicy.getInsuredPersons(),
                        storedPolicy.getExternalId(),
                        storedPolicy.getStartDate());
    }
}

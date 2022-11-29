package yeter.ugur.insuranceexample.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.time.Clock;
import java.util.List;

import static yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper.toInsuredPersonEntities;
import static yeter.ugur.insuranceexample.service.mapper.PolicyCreationResponseDtoMapper.toPolicyCreationResponseDto;

@Slf4j
@Service
public class PolicyCreationService {

    private final ExternalPolicyIdGenerator externalPolicyIdGenerator;
    private final PolicyRepository policyRepository;
    private final InsuredPersonRepository insuredPersonRepository;
    private final Clock clock;

    public PolicyCreationService(ExternalPolicyIdGenerator externalPolicyIdGenerator,
                                 PolicyRepository policyRepository,
                                 InsuredPersonRepository insuredPersonRepository,
                                 Clock clock) {
        this.externalPolicyIdGenerator = externalPolicyIdGenerator;
        this.policyRepository = policyRepository;
        this.insuredPersonRepository = insuredPersonRepository;
        this.clock = clock;
    }

    @Transactional
    public PolicyCreationResponseDto createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = generateUniquePolicyId();
        PolicyEntity policyEntity = mapToPolicyEntity(creationRequestDto, externalPolicyId);
        List<InsuredPersonEntity> insuredPersons = toInsuredPersonEntities(creationRequestDto.getInsuredPersons());
        PolicyEntity storedPolicy = createPolicyWithInsuredPersons(policyEntity, insuredPersons);
        return toPolicyCreationResponseDto(storedPolicy.getInsuredPersons(), storedPolicy.getExternalId(), storedPolicy.getStartDate());
    }

    private PolicyEntity mapToPolicyEntity(PolicyCreationRequestDto creationRequestDto, String externalPolicyId) {
        return PolicyEntity.builder()
                .externalId(externalPolicyId)
                .startDate(creationRequestDto.getStartDate())
                .createdAt(clock.instant().toEpochMilli())
                .build();
    }


    @VisibleForTesting
    String generateUniquePolicyId() {
        boolean uniqueId;
        String generatedId;
        do {
            String candidate = externalPolicyIdGenerator.generate();
            uniqueId = policyRepository.findByExternalId(candidate).isEmpty();
            generatedId = candidate;
            // TODO max attempt count could be limited.
        } while (!uniqueId);
        return generatedId;
    }

    @VisibleForTesting
    PolicyEntity createPolicyWithInsuredPersons(PolicyEntity policyEntity, List<InsuredPersonEntity> insuredPersons) {
        PolicyEntity storedPolicy = policyRepository.save(policyEntity);
        if (!CollectionUtils.isEmpty(insuredPersons)) {
            List<InsuredPersonEntity> storedPersons = insuredPersonRepository.saveAll(insuredPersons);
            storedPolicy.addPersons(storedPersons);
        }

        return storedPolicy;
    }
}

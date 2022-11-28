package yeter.ugur.insuranceexample.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

import static yeter.ugur.insuranceexample.service.InsuredPersonMapper.toInsuredPersonEntities;

@Slf4j
@Service
public class PolicyCreationService {

    private final ExternalPolicyIdGenerator externalPolicyIdGenerator;
    private final PolicyRepository policyRepository;
    private final InsuredPersonRepository insuredPersonRepository;
    private final StorageHelper storageHelper;
    private final Clock clock;

    public PolicyCreationService(ExternalPolicyIdGenerator externalPolicyIdGenerator,
                                 PolicyRepository policyRepository,
                                 InsuredPersonRepository insuredPersonRepository, StorageHelper storageHelper,
                                 Clock clock) {
        this.externalPolicyIdGenerator = externalPolicyIdGenerator;
        this.policyRepository = policyRepository;
        this.insuredPersonRepository = insuredPersonRepository;
        this.storageHelper = storageHelper;
        this.clock = clock;
    }

    @Transactional
    public PolicyCreationResponseDto createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = generateUniquePolicyId();
        List<InsuredPersonEntity> insuredPersons = toInsuredPersonEntities(creationRequestDto.getInsuredPersons());
        PolicyEntity storedPolicy = createPolicyWithInsuredPersons(mapToPolicyEntity(creationRequestDto, externalPolicyId), insuredPersons);
        return PolicyCreationResponseDto.builder()
                .policyId(storedPolicy.getExternalId())
                .startDate(storedPolicy.getStartDate())
                .insuredPersons(InsuredPersonMapper.toInsuredPersonsDto(storedPolicy))
                .totalPremium(PolicyPremiumHelper.calculateTotalPremium(storedPolicy.getInsuredPersons()))
                .build();
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

    public PolicyEntity createPolicyWithInsuredPersons(PolicyEntity policyEntity, List<InsuredPersonEntity> insuredPersons) {
        PolicyEntity storedPolicy = policyRepository.save(policyEntity);
        List<InsuredPersonEntity> storedPersons = insuredPersons.stream()
                .map(insuredPersonRepository::save)
                .collect(Collectors.toList());
        storedPolicy.addPersons(storedPersons);
        return policyRepository.save(policyEntity);
    }
}

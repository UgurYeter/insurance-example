package yeter.ugur.insuranceexample.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.PolicyResponse;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PolicyManager {

    private final ExternalPolicyIdGenerator externalPolicyIdGenerator;
    private final PolicyRepository policyRepository;

    private final Clock clock;

    public PolicyManager(ExternalPolicyIdGenerator externalPolicyIdGenerator, PolicyRepository policyRepository, Clock clock) {
        this.externalPolicyIdGenerator = externalPolicyIdGenerator;
        this.policyRepository = policyRepository;
        this.clock = clock;
    }

    @Transactional
    public PolicyResponse createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = getUniqueExternalPolicyId();
        PolicyEntity storedPolicy = policyRepository.save(PolicyEntity.builder()
                .externalId(externalPolicyId)
                .effectiveDate(creationRequestDto.getStartDate())
                .createdAt(clock.instant().toEpochMilli())
                .build());

        List<InsuredPersonEntity> insuredPersons = creationRequestDto.getInsuredPersons()
                .stream()
                .map(person -> InsuredPersonEntity.builder()
                        .firstName(person.getFirstName())
                        .secondName(person.getSecondName())
                        .premium(person.getPremium())
                        .build())
                .collect(Collectors.toList());

        storedPolicy.addPersons(insuredPersons);
        PolicyEntity policyWithPersons = policyRepository.findById(storedPolicy.getId()).orElseThrow(
                () -> new RuntimeException("Unexpectedly can't find the policy with id:" + storedPolicy.getExternalId()));

        return PolicyResponse.builder()
                .policyId(storedPolicy.getExternalId())
                .effectiveDate(storedPolicy.getEffectiveDate())
                .insuredPersons(insuredPersons)
                .build();
    }

    @VisibleForTesting
    String getUniqueExternalPolicyId() {
        boolean existingId;
        String generatedId;
        do {
            String candidate = externalPolicyIdGenerator.generate();
            existingId = policyRepository.findByExternalId(candidate).isPresent();
            generatedId = candidate;
            // TODO max attempt count could be limited.
        } while (existingId);
        return generatedId;
    }
}

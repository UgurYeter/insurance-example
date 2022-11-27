package yeter.ugur.insuranceexample.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.InsuredPerson;
import yeter.ugur.insuranceexample.api.PolicyCreationRequest;
import yeter.ugur.insuranceexample.api.PolicyCreationResponse;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PolicyManager {

    private final ExternalPolicyIdGenerator externalPolicyIdGenerator;
    private final PolicyRepository policyRepository;
    private final PolicyInsuredPersonStorageHelper policyInsuredPersonStorageHelper;
    private final Clock clock;

    public PolicyManager(ExternalPolicyIdGenerator externalPolicyIdGenerator, PolicyRepository policyRepository, PolicyInsuredPersonStorageHelper policyInsuredPersonStorageHelper, Clock clock) {
        this.externalPolicyIdGenerator = externalPolicyIdGenerator;
        this.policyRepository = policyRepository;
        this.policyInsuredPersonStorageHelper = policyInsuredPersonStorageHelper;
        this.clock = clock;
    }

    public PolicyCreationResponse createPolicy(PolicyCreationRequest creationRequestDto) {
        String externalPolicyId = getUniqueExternalPolicyId();

        List<InsuredPersonEntity> insuredPersons = creationRequestDto.getInsuredPersons()
                .stream()
                .map(person -> InsuredPersonEntity.builder()
                        .firstName(person.getFirstName())
                        .secondName(person.getSecondName())
                        .premium(person.getPremium())
                        .build())
                .collect(Collectors.toList());

        PolicyEntity storedPolicy = policyInsuredPersonStorageHelper.createPolicyWithInsuredPersons(PolicyEntity.builder()
                .externalId(externalPolicyId)
                .effectiveDate(creationRequestDto.getStartDate())
                .createdAt(clock.instant().toEpochMilli())
                .build(), insuredPersons);

        return PolicyCreationResponse.builder()
                .policyId(storedPolicy.getExternalId())
                .startDate(storedPolicy.getEffectiveDate())
                .insuredPersons(storedPolicy.getInsuredPersons().stream()
                        .map(storedPerson -> InsuredPerson.builder()
                                .id(storedPerson.getId())
                                .firstName(storedPerson.getFirstName())
                                .secondName(storedPerson.getSecondName())
                                .premium(storedPerson.getPremium())
                                .build()).collect(Collectors.toList()))
                .totalPremium(storedPolicy.getInsuredPersons()
                        .stream()
                        .map(InsuredPersonEntity::getPremium)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
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

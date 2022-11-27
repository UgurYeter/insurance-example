package yeter.ugur.insuranceexample.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.api.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.api.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
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

    public PolicyCreationResponseDto createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = getUniqueExternalPolicyId();
        List<InsuredPersonEntity> insuredPersons = mapToPersonEntities(creationRequestDto);
        PolicyEntity storedPolicy = policyInsuredPersonStorageHelper
                .createPolicyWithInsuredPersons(mapToPolicyEntity(creationRequestDto, externalPolicyId), insuredPersons);
        return PolicyCreationResponseDto.builder()
                .policyId(storedPolicy.getExternalId())
                .startDate(storedPolicy.getEffectiveDate())
                .insuredPersons(storedPolicy.getInsuredPersons().stream()
                        .map(mapToInsuredPersonDto()).collect(Collectors.toList()))
                .totalPremium(calculateTotalPremium(storedPolicy))
                .build();
    }

    private static BigDecimal calculateTotalPremium(PolicyEntity storedPolicy) {
        return storedPolicy.getInsuredPersons()
                .stream()
                .map(InsuredPersonEntity::getPremium)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static Function<InsuredPersonEntity, InsuredPersonDto> mapToInsuredPersonDto() {
        return storedPerson -> InsuredPersonDto.builder()
                .id(storedPerson.getId())
                .firstName(storedPerson.getFirstName())
                .secondName(storedPerson.getSecondName())
                .premium(storedPerson.getPremium())
                .build();
    }

    private PolicyEntity mapToPolicyEntity(PolicyCreationRequestDto creationRequestDto, String externalPolicyId) {
        return PolicyEntity.builder()
                .externalId(externalPolicyId)
                .effectiveDate(creationRequestDto.getStartDate())
                .createdAt(clock.instant().toEpochMilli())
                .build();
    }

    private static List<InsuredPersonEntity> mapToPersonEntities(PolicyCreationRequestDto creationRequestDto) {
        return creationRequestDto.getInsuredPersons()
                .stream()
                .map(person -> InsuredPersonEntity.builder()
                        .firstName(person.getFirstName())
                        .secondName(person.getSecondName())
                        .premium(person.getPremium())
                        .build())
                .collect(Collectors.toList());
    }

    @VisibleForTesting
    String getUniqueExternalPolicyId() {
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

    public void modifyPolicy(PolicyModificationRequestDto policyModificationRequestDto) {
        String externalPolicyId = policyModificationRequestDto.getPolicyId();
        List<PolicyEntity> policies = policyRepository.findByExternalId(externalPolicyId);
        List<PolicyEntity> effectedPolicies = policies.stream()
                .filter(policy -> policy.getEffectiveDate().isEqual(policyModificationRequestDto.getEffectiveDate()) ||
                        policy.getEffectiveDate().isAfter(policyModificationRequestDto.getEffectiveDate()))
                .collect(Collectors.toList());
        List<InsuredPersonDto> additionalPersons = policyModificationRequestDto.getInsuredPersons()
                .stream().filter(person -> Objects.isNull(person.getId()))
                .collect(Collectors.toList());

        List<InsuredPersonDto> personsToMigrateToNewVersion = policyModificationRequestDto.getInsuredPersons()
                .stream().filter(person -> Objects.nonNull(person.getId()))
                .collect(Collectors.toList());

    }
}

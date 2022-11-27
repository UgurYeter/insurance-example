package yeter.ugur.insuranceexample.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.api.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.api.PolicyModificationException;
import yeter.ugur.insuranceexample.api.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.api.PolicyModificationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PolicyManager {

    public static final LocalDate NOW = LocalDate.now();
    private final ExternalPolicyIdGenerator externalPolicyIdGenerator;
    private final PolicyRepository policyRepository;
    private final InsuredPersonRepository insuredPersonRepository;
    private final PolicyInsuredPersonStorageHelper policyInsuredPersonStorageHelper;
    private final Clock clock;

    public PolicyManager(ExternalPolicyIdGenerator externalPolicyIdGenerator, PolicyRepository policyRepository, InsuredPersonRepository insuredPersonRepository, PolicyInsuredPersonStorageHelper policyInsuredPersonStorageHelper, Clock clock) {
        this.externalPolicyIdGenerator = externalPolicyIdGenerator;
        this.policyRepository = policyRepository;
        this.insuredPersonRepository = insuredPersonRepository;
        this.policyInsuredPersonStorageHelper = policyInsuredPersonStorageHelper;
        this.clock = clock;
    }

    public PolicyCreationResponseDto createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = getUniqueExternalPolicyId();
        List<InsuredPersonEntity> insuredPersons = mapToPersonEntities(creationRequestDto.getInsuredPersons());
        PolicyEntity storedPolicy = policyInsuredPersonStorageHelper
                .createPolicyWithInsuredPersons(mapToPolicyEntity(creationRequestDto, externalPolicyId), insuredPersons);
        return PolicyCreationResponseDto.builder()
                .policyId(storedPolicy.getExternalId())
                .startDate(storedPolicy.getStartDate())
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
                .startDate(creationRequestDto.getStartDate())
                .createdAt(clock.instant().toEpochMilli())
                .build();
    }

    private static List<InsuredPersonEntity> mapToPersonEntities(List<InsuredPersonDto> insuredPersons) {
        return insuredPersons
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

    public PolicyModificationResponseDto modifyPolicy(PolicyModificationRequestDto policyModificationRequestDto) {
        PolicyEntity latestPolicyState = findLatestPolicyState(policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate())
                .orElseThrow(() -> new PolicyModificationException("Can't find policy to modify!"));
        List<InsuredPersonEntity> personsForNewPolicy = insuredPersonRepository.findAllById(
                getPersonIdsInModificationRequest(policyModificationRequestDto));
        List<InsuredPersonDto> newInsurancePersonsToCreate = getPersonsToAddFromModificationRequest(policyModificationRequestDto);
        if (!newInsurancePersonsToCreate.isEmpty()) {
            List<InsuredPersonEntity> insuredPersonEntities = mapToPersonEntities(newInsurancePersonsToCreate);
            List<InsuredPersonEntity> newlyCreatedPersons = insuredPersonRepository.saveAll(insuredPersonEntities);
            personsForNewPolicy.addAll(newlyCreatedPersons);
        }
        PolicyEntity newPolicyState = PolicyEntity.builder()
                .externalId(latestPolicyState.getExternalId())
                .createdAt(clock.instant().toEpochMilli())
                .startDate(policyModificationRequestDto.getEffectiveDate())
                .build();
        newPolicyState.addPersons(personsForNewPolicy);
        newPolicyState = policyRepository.save(newPolicyState);
        return PolicyModificationResponseDto.builder()
                .policyId(newPolicyState.getExternalId())
                .effectiveDate(newPolicyState.getStartDate())
                .insuredPersons(newPolicyState.getInsuredPersons().stream()
                        .map(mapToInsuredPersonDto()).collect(Collectors.toList()))
                .totalPremium(calculateTotalPremium(newPolicyState))
                .build();
    }

    private List<Integer> getPersonIdsInModificationRequest(PolicyModificationRequestDto policyModificationRequestDto) {
        return policyModificationRequestDto.getInsuredPersons()
                .stream()
                .map(InsuredPersonDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<InsuredPersonDto> getPersonsToAddFromModificationRequest(PolicyModificationRequestDto policyModificationRequestDto) {
        return policyModificationRequestDto.getInsuredPersons()
                .stream()
                .filter(person -> Objects.isNull(person.getId()))
                .collect(Collectors.toList());
    }

    private Optional<PolicyEntity> findLatestPolicyState(String policyId, LocalDate effectiveDate) {
        List<PolicyEntity> foundPolicies = policyRepository.findByExternalId(policyId);
        if (foundPolicies.isEmpty()) {
            return Optional.empty();
        }
        List<PolicyEntity> filteredPolicies = new ArrayList<>();
        for (PolicyEntity policyEntity : foundPolicies) {
            if (policyEntity.getStartDate().isBefore(effectiveDate)
                    || policyEntity.getStartDate().isEqual(effectiveDate)) {
                filteredPolicies.add(policyEntity);
            }
        }
        if (filteredPolicies.isEmpty()) {
            return Optional.empty();
        }
        filteredPolicies.sort(Comparator.comparing(PolicyEntity::getStartDate));
        return Optional.of(filteredPolicies.get(foundPolicies.size() - 1));
    }
}

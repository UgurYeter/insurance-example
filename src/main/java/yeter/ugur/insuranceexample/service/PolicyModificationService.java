package yeter.ugur.insuranceexample.service;

import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static yeter.ugur.insuranceexample.service.InsuredPersonMapper.toInsuredPersonEntities;

@Service
public class PolicyModificationService {
    private final InsuredPersonRepository insuredPersonRepository;
    private final PolicyRepository policyRepository;
    private final PolicyStateHelper policyStateHelper;
    private final Clock clock;

    public PolicyModificationService(InsuredPersonRepository insuredPersonRepository,
                                     PolicyRepository policyRepository,
                                     PolicyStateHelper policyStateHelper,
                                     Clock clock) {
        this.insuredPersonRepository = insuredPersonRepository;
        this.policyRepository = policyRepository;
        this.policyStateHelper = policyStateHelper;
        this.clock = clock;
    }

    public PolicyModificationResponseDto modifyPolicy(PolicyModificationRequestDto policyModificationRequestDto) {
        PolicyEntity basePolicy =
                policyStateHelper.findLatestPolicyStatePriorToDate(policyModificationRequestDto.getPolicyId(),
                                policyModificationRequestDto.getEffectiveDate())
                        .orElseThrow(() -> new PolicyIsNotFoundException("Can't find policy to modify!"));

        List<InsuredPersonEntity> personsOfModifiedPolicy = getPersonsOfModifiedPolicy(
                toInsuredPersonEntities(policyModificationRequestDto.getInsuredPersons()));
        PolicyEntity newPolicyState = PolicyEntity.builder()
                .externalId(basePolicy.getExternalId())
                .createdAt(clock.instant().toEpochMilli())
                .startDate(policyModificationRequestDto.getEffectiveDate())
                .build();
        newPolicyState.addPersons(personsOfModifiedPolicy);
        newPolicyState = policyRepository.save(newPolicyState);
        return PolicyModificationResponseDto.builder()
                .policyId(newPolicyState.getExternalId())
                .effectiveDate(newPolicyState.getStartDate())
                .insuredPersons(InsuredPersonMapper.toInsuredPersonsDto(newPolicyState.getInsuredPersons()))
                .totalPremium(PolicyPremiumHelper.calculateTotalPremium(newPolicyState.getInsuredPersons()))
                .build();
    }

    private List<InsuredPersonEntity> getPersonsOfModifiedPolicy(List<InsuredPersonEntity> insuredPersonEntities) {
        List<InsuredPersonEntity> existingPersons = insuredPersonRepository
                .findAllById(collectPersonIds(insuredPersonEntities));
        List<InsuredPersonEntity> personsOfModifiedPolicy = new ArrayList<>(existingPersons);
        List<InsuredPersonEntity> newInsurancePersonsToCreate = collectPersonsWithNullId(insuredPersonEntities);
        if (!newInsurancePersonsToCreate.isEmpty()) {
            List<InsuredPersonEntity> newlyCreatedPersons = insuredPersonRepository.saveAll(newInsurancePersonsToCreate);
            personsOfModifiedPolicy.addAll(newlyCreatedPersons);
        }
        return personsOfModifiedPolicy;
    }

    @VisibleForTesting
    List<Integer> collectPersonIds(List<InsuredPersonEntity> insuredPersons) {
        return insuredPersons
                .stream()
                .map(InsuredPersonEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @VisibleForTesting
    List<InsuredPersonEntity> collectPersonsWithNullId(List<InsuredPersonEntity> insuredPersons) {
        return insuredPersons
                .stream()
                .filter(person -> Objects.isNull(person.getId()))
                .collect(Collectors.toList());
    }
}

package yeter.ugur.insuranceexample.service;

import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;
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

import static yeter.ugur.insuranceexample.service.InsuredPersonMapper.collectPersonIds;
import static yeter.ugur.insuranceexample.service.InsuredPersonMapper.mapToPersonEntities;

@Service
public class PolicyModificationService {
    private final InsuredPersonRepository insuredPersonRepository;
    private final PolicyRepository policyRepository;
    private final StorageHelper storageHelper;
    private final Clock clock;

    public PolicyModificationService(InsuredPersonRepository insuredPersonRepository,
                                     PolicyRepository policyRepository,
                                     StorageHelper storageHelper,
                                     Clock clock) {
        this.insuredPersonRepository = insuredPersonRepository;
        this.policyRepository = policyRepository;
        this.storageHelper = storageHelper;
        this.clock = clock;
    }

    public PolicyModificationResponseDto modifyPolicy(PolicyModificationRequestDto policyModificationRequestDto) {
        PolicyEntity basePolicy =
                storageHelper.findLatestStoredPolicyPriorToDate(policyModificationRequestDto.getPolicyId(),
                                policyModificationRequestDto.getEffectiveDate())
                        .orElseThrow(() -> new PolicyIsNotFoundException("Can't find policy to modify!"));

        List<InsuredPersonEntity> personsOfModifiedPolicy = getPersonsOfModifiedPolicy(policyModificationRequestDto);
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
                .insuredPersons(InsuredPersonMapper.toInsuredPersonsDto(newPolicyState))
                .totalPremium(PolicyPremiumHelper.calculateTotalPremium(newPolicyState.getInsuredPersons()))
                .build();
    }

    private List<InsuredPersonEntity> getPersonsOfModifiedPolicy(PolicyModificationRequestDto policyModificationRequestDto) {
        List<InsuredPersonEntity> existingPersons = insuredPersonRepository
                .findAllById(collectPersonIds(policyModificationRequestDto.getInsuredPersons()));
        List<InsuredPersonEntity> personsOfModifiedPolicy = new ArrayList<>(existingPersons);
        List<InsuredPersonDto> newInsurancePersonsToCreate = collectPersonsWithNullId(policyModificationRequestDto.getInsuredPersons());
        if (!newInsurancePersonsToCreate.isEmpty()) {
            List<InsuredPersonEntity> insuredPersonEntities = mapToPersonEntities(newInsurancePersonsToCreate);
            List<InsuredPersonEntity> newlyCreatedPersons = insuredPersonRepository.saveAll(insuredPersonEntities);
            personsOfModifiedPolicy.addAll(newlyCreatedPersons);
        }
        return personsOfModifiedPolicy;
    }

    private List<InsuredPersonDto> collectPersonsWithNullId(List<InsuredPersonDto> insuredPersons) {
        return insuredPersons
                .stream()
                .filter(person -> Objects.isNull(person.getId()))
                .collect(Collectors.toList());
    }
}

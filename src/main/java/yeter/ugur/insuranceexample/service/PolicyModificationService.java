package yeter.ugur.insuranceexample.service;

import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.helper.PolicyPremiumHelper;
import yeter.ugur.insuranceexample.service.helper.PolicyStateHelper;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper.toInsuredPersonEntities;

@Service
public class PolicyModificationService {
    private final InsuredPersonRepository insuredPersonRepository;
    private final PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper;
    private final PolicyStateHelper policyStateHelper;
    private final Clock clock;

    public PolicyModificationService(InsuredPersonRepository insuredPersonRepository,
                                     PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper,
                                     PolicyStateHelper policyStateHelper,
                                     Clock clock) {
        this.insuredPersonRepository = insuredPersonRepository;
        this.policyAndInsuredPersonStorageHelper = policyAndInsuredPersonStorageHelper;
        this.policyStateHelper = policyStateHelper;
        this.clock = clock;
    }

    public PolicyModificationResponseDto modifyPolicy(PolicyModificationRequestDto policyModificationRequestDto) {
        PolicyEntity basePolicy =
                policyStateHelper.findLatestPolicyStatePriorToDate(policyModificationRequestDto.getPolicyId(),
                                policyModificationRequestDto.getEffectiveDate())
                        .orElseThrow(() -> new PolicyIsNotFoundException("Can't find policy to modify!"));

        List<InsuredPersonEntity> insuredPersonEntities = toInsuredPersonEntities(policyModificationRequestDto.getInsuredPersons());
        Set<Integer> personIdsInTheRequest = collectPersonIds(toInsuredPersonEntities(policyModificationRequestDto.getInsuredPersons()));
        List<InsuredPersonEntity> existingPersons = basePolicy.getInsuredPersons()
                .stream()
                .filter(person -> personIdsInTheRequest.contains(person.getId()))
                .collect(Collectors.toList());

        List<InsuredPersonEntity> newInsurancePersonsToCreate = collectPersonsWithNullId(insuredPersonEntities);
        PolicyEntity newPolicyState = buildPolicyEntity(policyModificationRequestDto.getEffectiveDate(), basePolicy.getExternalId());
        newPolicyState = policyAndInsuredPersonStorageHelper.createPolicyWithInsuredPersons(newPolicyState, newInsurancePersonsToCreate);
        newPolicyState.addPersons(existingPersons);
        if(basePolicy.getStartDate().isEqual(policyModificationRequestDto.getEffectiveDate())){
            policyStateHelper.deleteById(basePolicy.getId());
        }
        return PolicyModificationResponseDto.builder()
                .policyId(newPolicyState.getExternalId())
                .effectiveDate(newPolicyState.getStartDate())
                .insuredPersons(InsuredPersonMapper.toInsuredPersonsDto(newPolicyState.getInsuredPersons()))
                .totalPremium(PolicyPremiumHelper.calculateTotalPremium(newPolicyState.getInsuredPersons()))
                .build();
    }

    private PolicyEntity buildPolicyEntity(LocalDate effectiveDate, String externalId) {
        return PolicyEntity.builder()
                .externalId(externalId)
                .createdAt(clock.instant().toEpochMilli())
                .startDate(effectiveDate)
                .build();
    }

    @VisibleForTesting
    Set<Integer> collectPersonIds(List<InsuredPersonEntity> insuredPersons) {
        return insuredPersons
                .stream()
                .map(InsuredPersonEntity::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @VisibleForTesting
    List<InsuredPersonEntity> collectPersonsWithNullId(List<InsuredPersonEntity> insuredPersons) {
        return insuredPersons
                .stream()
                .filter(person -> Objects.isNull(person.getId()))
                .collect(Collectors.toList());
    }
}

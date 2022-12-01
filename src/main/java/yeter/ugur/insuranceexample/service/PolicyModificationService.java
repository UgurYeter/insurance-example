package yeter.ugur.insuranceexample.service;

import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;
import yeter.ugur.insuranceexample.api.modification.CollidingPolicyEffectiveDate;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.helper.PolicyPremiumHelper;
import yeter.ugur.insuranceexample.service.helper.PolicyStateHelper;
import yeter.ugur.insuranceexample.service.helper.TimeHelper;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PolicyModificationService {
    private final PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper;
    private final PolicyStateHelper policyStateHelper;
    private final TimeHelper timeHelper;
    private final InsuredPersonMapper insuredPersonMapper;

    public PolicyModificationService(PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper,
                                     PolicyStateHelper policyStateHelper,
                                     TimeHelper timeHelper,
                                     InsuredPersonMapper insuredPersonMapper) {
        this.policyAndInsuredPersonStorageHelper = policyAndInsuredPersonStorageHelper;
        this.policyStateHelper = policyStateHelper;
        this.timeHelper = timeHelper;
        this.insuredPersonMapper = insuredPersonMapper;
    }


    public PolicyModificationResponseDto modifyPolicy(PolicyModificationRequestDto policyModificationRequestDto) {
        PolicyEntity basePolicy =
                policyStateHelper.findLatestPolicyStatePriorToDate(policyModificationRequestDto.getPolicyId(),
                                policyModificationRequestDto.getEffectiveDate())
                        .orElseThrow(() -> new PolicyIsNotFoundException("Can't find policy to modify!"));
        if (basePolicy.getStartDate().isEqual(policyModificationRequestDto.getEffectiveDate())) {
            throw new CollidingPolicyEffectiveDate("There is already a policy state for the effective date!");
        }

        Set<Integer> personIdsInTheRequest = collectPersonIds(policyModificationRequestDto.getInsuredPersons());
        List<InsuredPersonEntity> existingPersons = basePolicy.getInsuredPersons()
                .stream()
                .filter(person -> personIdsInTheRequest.contains(person.getId()))
                .collect(Collectors.toList());

        PolicyEntity policyWithNewlyCreatedPersons = createNewState(
                policyModificationRequestDto,
                existingPersons,
                basePolicy.getExternalId());
        return prepareResponse(policyWithNewlyCreatedPersons);
    }

    private PolicyEntity createNewState(PolicyModificationRequestDto policyModificationRequestDto,
                                        List<InsuredPersonEntity> existingPersons,
                                        String policyExternalId) {
        List<InsuredPersonDto> newInsurancePersonsToCreate = collectPersonsWithNullId(policyModificationRequestDto.getInsuredPersons());
        PolicyEntity newPolicyState = buildPolicyEntity(policyModificationRequestDto.getEffectiveDate(), policyExternalId);
        PolicyEntity policyWithNewlyCreatedPersons = policyAndInsuredPersonStorageHelper.createPolicyWithInsuredPersons(
                newPolicyState,
                insuredPersonMapper.toInsuredPersonEntities(newInsurancePersonsToCreate)
        );
        policyWithNewlyCreatedPersons.addPersons(existingPersons);
        return policyWithNewlyCreatedPersons;
    }

    private PolicyModificationResponseDto prepareResponse(PolicyEntity policyWithNewlyCreatedPersons) {
        List<InsuredPersonEntity> insuredPersons = policyWithNewlyCreatedPersons.getInsuredPersons();
        return PolicyModificationResponseDto.builder()
                .policyId(policyWithNewlyCreatedPersons.getExternalId())
                .effectiveDate(policyWithNewlyCreatedPersons.getStartDate())
                .insuredPersons(insuredPersonMapper.toInsuredPersonsDto(insuredPersons))
                .totalPremium(PolicyPremiumHelper.calculateTotalPremium(insuredPersons))
                .build();
    }

    private PolicyEntity buildPolicyEntity(LocalDate effectiveDate, String externalId) {
        return PolicyEntity.builder()
                .externalId(externalId)
                .createdAt(timeHelper.getCurrentMilliSecond())
                .startDate(effectiveDate)
                .build();
    }

    @VisibleForTesting
    Set<Integer> collectPersonIds(List<InsuredPersonDto> insuredPersons) {
        return insuredPersons
                .stream()
                .map(InsuredPersonDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @VisibleForTesting
    List<InsuredPersonDto> collectPersonsWithNullId(List<InsuredPersonDto> insuredPersons) {
        return insuredPersons
                .stream()
                .filter(person -> Objects.isNull(person.getId()))
                .collect(Collectors.toList());
    }
}

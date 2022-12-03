package yeter.ugur.insuranceexample.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;
import yeter.ugur.insuranceexample.api.modification.CollidingPolicyEffectiveDate;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.helper.PolicyStateHelper;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;
import yeter.ugur.insuranceexample.service.mapper.PolicyObjectsMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PolicyModificationService {
    private final PolicyStateHelper policyStateHelper;
    private final PolicyObjectsMapper policyObjectsMapper;
    private final InsuredPersonMapper insuredPersonMapper;

    public PolicyModificationService(
            PolicyStateHelper policyStateHelper,
            PolicyObjectsMapper policyObjectsMapper,
            InsuredPersonMapper insuredPersonMapper) {
        this.policyStateHelper = policyStateHelper;
        this.policyObjectsMapper = policyObjectsMapper;
        this.insuredPersonMapper = insuredPersonMapper;
    }


    public PolicyModificationResponseDto modifyPolicy(PolicyModificationRequestDto policyModificationRequestDto) {
        checkUniquenessOrThrow(
                policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate());
        PolicyEntity policyEffectiveToday =
                policyStateHelper.findLatestPolicyStatePriorOrEqualToDate(
                                policyModificationRequestDto.getPolicyId(),
                                policyModificationRequestDto.getEffectiveDate())
                        .orElseThrow(() -> new PolicyIsNotFoundException("Can't find policy to modify!"));

        Set<Integer> effectivePolicyPersonIds = collectEffectivePolicyPersonIds(policyEffectiveToday);
        List<InsuredPersonEntity> insuredPersonsOfNewPolicyState =
                insuredPersonMapper
                        .toInsuredPersonEntities(policyModificationRequestDto.getInsuredPersons())
                        .stream()
                        .filter(insuredPersonEntity -> personIdNullOrInTheSet(effectivePolicyPersonIds, insuredPersonEntity))
                        .collect(Collectors.toList());

        PolicyEntity newState = createNewState(
                policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate(),
                insuredPersonsOfNewPolicyState);
        PolicyModificationResponseDto policyModificationResponseDto = policyObjectsMapper.toPolicyModificationResponseDto(newState);
        log.info("Policy with id:'{}' and external_id:'{}' successfully modified!", newState.getId(), newState.getExternalId());
        return policyModificationResponseDto;
    }

    private static Set<Integer> collectEffectivePolicyPersonIds(PolicyEntity policyEffectiveToday) {
        return policyEffectiveToday.getInsuredPersons()
                .stream()
                .map(InsuredPersonEntity::getId)
                .collect(Collectors.toSet());
    }


    private boolean personIdNullOrInTheSet(Set<Integer> effectivePolicyPersonIds,
                                           InsuredPersonEntity insuredPersonEntity) {
        return Objects.isNull(insuredPersonEntity.getId())
                || effectivePolicyPersonIds.contains(insuredPersonEntity.getId());
    }

    private void checkUniquenessOrThrow(String externalPolicyId, LocalDate effectiveDate) {
        if (policyStateHelper.findPolicyStateByExternalIdAndStartDate(externalPolicyId, effectiveDate).isPresent()) {
            throw new CollidingPolicyEffectiveDate("There is already a policy state for the sent effective date, "
                    + "thus this policy modification is not valid!");
        }
    }


    private PolicyEntity createNewState(String policyExternalId,
                                        LocalDate effectiveDate,
                                        List<InsuredPersonEntity> insuredPersonEntities) {
        PolicyEntity newPolicyState = policyObjectsMapper.mapToPolicyEntityWithoutInsuredPersons(policyExternalId, effectiveDate);
        return policyStateHelper.createNewPolicyState(
                newPolicyState,
                insuredPersonEntities
        );
    }
}

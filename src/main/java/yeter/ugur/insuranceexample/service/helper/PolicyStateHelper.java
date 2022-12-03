package yeter.ugur.insuranceexample.service.helper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PolicyStateHelper {

    private final PolicyRepository policyRepository;

    private final InsuredPersonRepository insuredPersonRepository;

    public PolicyStateHelper(PolicyRepository policyRepository,
                             InsuredPersonRepository insuredPersonRepository) {
        this.policyRepository = policyRepository;
        this.insuredPersonRepository = insuredPersonRepository;
    }

    public Optional<PolicyEntity> findLatestPolicyStatePriorOrEqualToDate(String externalPolicyId, LocalDate startDate) {
        List<PolicyEntity> foundPolicies = policyRepository.findByExternalId(externalPolicyId);
        if (foundPolicies.isEmpty()) {
            return Optional.empty();
        }
        List<PolicyEntity> priorOrEqualDatedStates = getPriorOrEqualDatedStates(startDate, foundPolicies);
        if (priorOrEqualDatedStates.isEmpty()) {
            return Optional.empty();
        }
        priorOrEqualDatedStates.sort(Comparator.comparing(PolicyEntity::getStartDate));
        return Optional.of(priorOrEqualDatedStates.get(priorOrEqualDatedStates.size() - 1));
    }

    private List<PolicyEntity> getPriorOrEqualDatedStates(LocalDate startDate, List<PolicyEntity> foundPolicies) {
        List<PolicyEntity> priorOrEqualDatedStates = new ArrayList<>();
        for (PolicyEntity policyEntity : foundPolicies) {
            if (policyEntity.getStartDate().isBefore(startDate)
                    || policyEntity.getStartDate().isEqual(startDate)) {
                priorOrEqualDatedStates.add(policyEntity);
            }
        }
        return priorOrEqualDatedStates;
    }

    public Optional<PolicyEntity> findPolicyStateByExternalIdAndStartDate(String externalPolicyId, LocalDate startDate) {
        return this.policyRepository.findByExternalIdAndStartDate(externalPolicyId, startDate);
    }

    @Transactional
    public PolicyEntity createNewPolicyState(PolicyEntity policyEntity, List<InsuredPersonEntity> insuredPersons) {
        if (!CollectionUtils.isEmpty(insuredPersons)) {
            List<InsuredPersonEntity> storedPersons = insuredPersonRepository.saveAll(insuredPersons);
            policyEntity.addPersons(storedPersons);
        }
        PolicyEntity savedPolicy = policyRepository.save(policyEntity);
        log.info("Policy with id:'{}' and external_id:'{}' together with person_ids:'{}' successfully stored!",
                savedPolicy.getId(),
                savedPolicy.getExternalId(),
                getStoredPersonIds(savedPolicy));
        return savedPolicy;
    }

    private Set<Integer> getStoredPersonIds(PolicyEntity savedPolicy) {
        return savedPolicy.getInsuredPersons()
                .stream()
                .map(InsuredPersonEntity::getId)
                .collect(Collectors.toSet());
    }

}

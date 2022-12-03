package yeter.ugur.insuranceexample.service.helper;

import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class PolicyStateHelper {

    private final PolicyRepository policyRepository;

    public PolicyStateHelper(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
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

}

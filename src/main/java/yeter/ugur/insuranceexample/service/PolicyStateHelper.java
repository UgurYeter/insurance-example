package yeter.ugur.insuranceexample.service;

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

    public Optional<PolicyEntity> findLatestPolicyStatePriorToDate(String policyId, LocalDate startDate) {
        List<PolicyEntity> foundPolicies = policyRepository.findByExternalId(policyId);
        if (foundPolicies.isEmpty()) {
            return Optional.empty();
        }
        List<PolicyEntity> filteredPolicies = new ArrayList<>();
        for (PolicyEntity policyEntity : foundPolicies) {
            if (policyEntity.getStartDate().isBefore(startDate)
                    || policyEntity.getStartDate().isEqual(startDate)) {
                filteredPolicies.add(policyEntity);
            }
        }
        if (filteredPolicies.isEmpty()) {
            return Optional.empty();
        }
        filteredPolicies.sort(Comparator.comparing(PolicyEntity::getStartDate));
        PolicyEntity latestMatchingPolicy = filteredPolicies.get(filteredPolicies.size() - 1);
        return Optional.of(latestMatchingPolicy);
    }

    public void deleteById(int id) {
        policyRepository.deleteById(id);
    }
}

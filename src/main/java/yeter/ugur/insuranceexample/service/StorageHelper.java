package yeter.ugur.insuranceexample.service;

import jakarta.transaction.Transactional;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StorageHelper {

    private final PolicyRepository policyRepository;
    private final InsuredPersonRepository insuredPersonRepository;

    public StorageHelper(PolicyRepository policyRepository, InsuredPersonRepository insuredPersonRepository) {
        this.policyRepository = policyRepository;
        this.insuredPersonRepository = insuredPersonRepository;
    }

    @VisibleForTesting
    public Optional<PolicyEntity> findLatestStoredPolicyPriorToDate(String policyId, LocalDate startDate) {
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
}

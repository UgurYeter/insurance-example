package yeter.ugur.insuranceexample.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.PolicyResponse;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.time.Clock;

@Slf4j
@Service
public class PolicyManager {

    private final ExternalPolicyIdGenerator externalPolicyIdGenerator;
    private final PolicyRepository policyRepository;

    private final Clock clock;

    public PolicyManager(ExternalPolicyIdGenerator externalPolicyIdGenerator, PolicyRepository policyRepository, Clock clock) {
        this.externalPolicyIdGenerator = externalPolicyIdGenerator;
        this.policyRepository = policyRepository;
        this.clock = clock;
    }

    public PolicyResponse createPolicy(PolicyCreationRequestDto creationRequestDto) {
        String externalPolicyId = getUniqueExternalPolicyId();
        PolicyEntity storedPolicy = policyRepository.save(PolicyEntity.builder()
                .externalId(externalPolicyId)
                .effectiveDate(creationRequestDto.getStartDate())
                .createdAt(clock.instant().toEpochMilli())
                .build());

        return PolicyResponse.builder()
                .policyId(storedPolicy.getExternalId())
                .build();
    }

    @VisibleForTesting
    String getUniqueExternalPolicyId() {
        boolean existingId;
        String generatedId;
        do {
            String candidate = externalPolicyIdGenerator.generate();
            existingId = policyRepository.findByExternalId(candidate).isPresent();
            generatedId = candidate;
            // TODO max attempt count could be limited.
        } while (existingId);
        return generatedId;
    }
}

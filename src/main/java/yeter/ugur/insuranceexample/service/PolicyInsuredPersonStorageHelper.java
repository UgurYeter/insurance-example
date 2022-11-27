package yeter.ugur.insuranceexample.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.util.List;

@Component
public class PolicyInsuredPersonStorageHelper {

    private final PolicyRepository policyRepository;

    public PolicyInsuredPersonStorageHelper(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Transactional
    public PolicyEntity createPolicyWithInsuredPersons(PolicyEntity policyEntity, List<InsuredPersonEntity> insuredPersons) {
        PolicyEntity storedPolicy = policyRepository.save(policyEntity);
        storedPolicy.addPersons(insuredPersons);
        return storedPolicy;
    }
}

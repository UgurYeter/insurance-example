package yeter.ugur.insuranceexample.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PolicyInsuredPersonStorageHelper {

    private final PolicyRepository policyRepository;
    private final InsuredPersonRepository insuredPersonRepository;

    public PolicyInsuredPersonStorageHelper(PolicyRepository policyRepository, InsuredPersonRepository insuredPersonRepository) {
        this.policyRepository = policyRepository;
        this.insuredPersonRepository = insuredPersonRepository;
    }

    @Transactional
    public PolicyEntity createPolicyWithInsuredPersons(PolicyEntity policyEntity, List<InsuredPersonEntity> insuredPersons) {
        PolicyEntity storedPolicy = policyRepository.save(policyEntity);
        List<InsuredPersonEntity> storedPersons = insuredPersons.stream()
                .map(insuredPersonRepository::save)
                .collect(Collectors.toList());
        storedPolicy.addPersons(storedPersons);
        return storedPolicy;
    }
}

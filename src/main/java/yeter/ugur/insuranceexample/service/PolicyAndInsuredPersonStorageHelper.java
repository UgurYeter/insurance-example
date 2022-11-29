package yeter.ugur.insuranceexample.service;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.InsuredPersonRepository;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.util.List;

@Component
public class PolicyAndInsuredPersonStorageHelper {
    private final PolicyRepository policyRepository;
    private final InsuredPersonRepository insuredPersonRepository;

    public PolicyAndInsuredPersonStorageHelper(PolicyRepository policyRepository, InsuredPersonRepository insuredPersonRepository) {
        this.policyRepository = policyRepository;
        this.insuredPersonRepository = insuredPersonRepository;
    }

    public PolicyEntity createPolicyWithInsuredPersons(PolicyEntity policyEntity, List<InsuredPersonEntity> insuredPersons) {
        PolicyEntity storedPolicy = policyRepository.save(policyEntity);
        if (!CollectionUtils.isEmpty(insuredPersons)) {
            List<InsuredPersonEntity> storedPersons = insuredPersonRepository.saveAll(insuredPersons);
            storedPolicy.addPersons(storedPersons);
        }

        return storedPolicy;
    }
}

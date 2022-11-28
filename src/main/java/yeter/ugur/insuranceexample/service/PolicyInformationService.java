package yeter.ugur.insuranceexample.service;

import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;
import yeter.ugur.insuranceexample.api.information.PolicyInformationResponseDto;
import yeter.ugur.insuranceexample.dao.PolicyEntity;

import java.time.LocalDate;

import static yeter.ugur.insuranceexample.service.PolicyPremiumHelper.calculateTotalPremium;

@Service
public class PolicyInformationService {
    private final StorageHelper storageHelper;

    public PolicyInformationService(StorageHelper storageHelper) {
        this.storageHelper = storageHelper;
    }

    public PolicyInformationResponseDto getPolicyInformation(String policyId, LocalDate requestLocalDate) {
        PolicyEntity foundPolicy = storageHelper.findLatestStoredPolicyPriorToDate(policyId, requestLocalDate)
                .orElseThrow(() -> new PolicyIsNotFoundException("Can't find policy to modify!"));

        return PolicyInformationResponseDto.builder()
                .policyId(foundPolicy.getExternalId())
                .requestDate(requestLocalDate)
                .insuredPersons(InsuredPersonMapper.toInsuredPersonsDto(foundPolicy))
                .totalPremium(calculateTotalPremium(foundPolicy.getInsuredPersons()))
                .build();
    }
}

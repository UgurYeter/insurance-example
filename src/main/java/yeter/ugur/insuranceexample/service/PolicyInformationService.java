package yeter.ugur.insuranceexample.service;

import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;
import yeter.ugur.insuranceexample.api.information.PolicyInformationResponseDto;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.helper.PolicyStateHelper;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;

import java.time.LocalDate;

import static yeter.ugur.insuranceexample.service.helper.PolicyPremiumHelper.calculateTotalPremium;

@Service
public class PolicyInformationService {
    private final PolicyStateHelper policyStateHelper;

    public PolicyInformationService(PolicyStateHelper policyStateHelper) {
        this.policyStateHelper = policyStateHelper;
    }

    public PolicyInformationResponseDto getPolicyInformation(String policyId, LocalDate requestLocalDate) {
        PolicyEntity foundPolicy = policyStateHelper.findLatestPolicyStatePriorToDate(policyId, requestLocalDate)
                .orElseThrow(() -> new PolicyIsNotFoundException("Can't find the policy!"));

        return PolicyInformationResponseDto.builder()
                .policyId(foundPolicy.getExternalId())
                .requestDate(requestLocalDate)
                .insuredPersons(InsuredPersonMapper.toInsuredPersonsDto(foundPolicy.getInsuredPersons()))
                .totalPremium(calculateTotalPremium(foundPolicy.getInsuredPersons()))
                .build();
    }
}

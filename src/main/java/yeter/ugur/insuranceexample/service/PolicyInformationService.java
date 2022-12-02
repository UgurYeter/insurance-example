package yeter.ugur.insuranceexample.service;

import org.springframework.stereotype.Service;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;
import yeter.ugur.insuranceexample.api.information.PolicyInformationResponseDto;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.helper.PolicyPremiumHelper;
import yeter.ugur.insuranceexample.service.helper.PolicyStateHelper;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;

import java.time.LocalDate;


@Service
public class PolicyInformationService {
    private final PolicyStateHelper policyStateHelper;
    private final InsuredPersonMapper insuredPersonMapper;
    private final PolicyPremiumHelper policyPremiumHelper;

    public PolicyInformationService(PolicyStateHelper policyStateHelper,
                                    InsuredPersonMapper insuredPersonMapper, PolicyPremiumHelper policyPremiumHelper) {
        this.policyStateHelper = policyStateHelper;
        this.insuredPersonMapper = insuredPersonMapper;
        this.policyPremiumHelper = policyPremiumHelper;
    }

    public PolicyInformationResponseDto getPolicyInformation(String policyId, LocalDate requestLocalDate) {
        PolicyEntity foundPolicy = policyStateHelper.findLatestPolicyStatePriorToDate(policyId, requestLocalDate)
                .orElseThrow(() -> new PolicyIsNotFoundException("Can't find the policy!"));
        PolicyInformationResponseDto policyInformationResponseDto = new PolicyInformationResponseDto();
        policyInformationResponseDto.setPolicyId(foundPolicy.getExternalId());
        policyInformationResponseDto.setRequestDate(requestLocalDate);
        policyInformationResponseDto.setInsuredPersons(insuredPersonMapper.toInsuredPersonsDto(foundPolicy.getInsuredPersons()));
        policyInformationResponseDto.setTotalPremium(policyPremiumHelper.calculateTotalPremium(foundPolicy.getInsuredPersons()));
        return policyInformationResponseDto;
    }
}

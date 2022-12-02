package yeter.ugur.insuranceexample.service.mapper;

import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.service.helper.PolicyPremiumHelper;
import yeter.ugur.insuranceexample.service.helper.TimeHelper;

import java.time.LocalDate;
import java.util.List;

@Component
public class PolicyObjectsMapper {

    private final InsuredPersonMapper insuredPersonMapper;
    private final TimeHelper timeHelper;
    private final PolicyPremiumHelper policyPremiumHelper;

    public PolicyObjectsMapper(InsuredPersonMapper insuredPersonMapper,
                               TimeHelper timeHelper,
                               PolicyPremiumHelper policyPremiumHelper) {
        this.insuredPersonMapper = insuredPersonMapper;
        this.timeHelper = timeHelper;
        this.policyPremiumHelper = policyPremiumHelper;
    }

    public PolicyCreationResponseDto toPolicyCreationResponseDto(List<InsuredPersonEntity> insuredPersons,
                                                                 String externalPolicyId,
                                                                 LocalDate startDate) {
        PolicyCreationResponseDto policyCreationResponseDto = new PolicyCreationResponseDto();
        policyCreationResponseDto.setPolicyId(externalPolicyId);
        policyCreationResponseDto.setStartDate(startDate);
        policyCreationResponseDto.setInsuredPersons(insuredPersonMapper.toInsuredPersonsDto(insuredPersons));
        policyCreationResponseDto.setTotalPremium(policyPremiumHelper.calculateTotalPremium(insuredPersons));
        return policyCreationResponseDto;
    }

    public PolicyEntity mapToPolicyEntityWithoutInsuredPersons(
            String externalPolicyId,
            LocalDate startDate) {
        return PolicyEntity.builder()
                .externalId(externalPolicyId)
                .startDate(startDate)
                .createdAt(timeHelper.getCurrentMilliSecond())
                .build();


    }

    public PolicyModificationResponseDto toPolicyModificationResponseDto(PolicyEntity policyWithNewlyCreatedPersons) {
        PolicyModificationResponseDto policyModificationResponseDto = new PolicyModificationResponseDto();
        policyModificationResponseDto.setPolicyId(policyWithNewlyCreatedPersons.getExternalId());
        policyModificationResponseDto.setEffectiveDate(policyWithNewlyCreatedPersons.getStartDate());
        policyModificationResponseDto.setInsuredPersons(insuredPersonMapper.toInsuredPersonsDto(policyWithNewlyCreatedPersons.getInsuredPersons()));
        policyModificationResponseDto.setTotalPremium(policyPremiumHelper.calculateTotalPremium(policyWithNewlyCreatedPersons.getInsuredPersons()));
        return policyModificationResponseDto;
    }
}

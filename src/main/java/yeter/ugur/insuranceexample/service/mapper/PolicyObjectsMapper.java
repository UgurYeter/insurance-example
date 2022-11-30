package yeter.ugur.insuranceexample.service.mapper;

import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
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

    public PolicyObjectsMapper(InsuredPersonMapper insuredPersonMapper, TimeHelper timeHelper) {
        this.insuredPersonMapper = insuredPersonMapper;
        this.timeHelper = timeHelper;
    }

    public PolicyCreationResponseDto toPolicyCreationResponseDto(List<InsuredPersonEntity> insuredPersons,
                                                                 String externalId,
                                                                 LocalDate startDate) {
        return PolicyCreationResponseDto.builder()
                .policyId(externalId)
                .startDate(startDate)
                .insuredPersons(insuredPersonMapper.toInsuredPersonsDto(insuredPersons))
                .totalPremium(PolicyPremiumHelper.calculateTotalPremium(insuredPersons))
                .build();
    }

    public PolicyEntity mapToPolicyEntityWithoutInsuredPersons(
            PolicyCreationRequestDto creationRequestDto,
            String externalPolicyId) {
        return PolicyEntity.builder()
                .externalId(externalPolicyId)
                .startDate(creationRequestDto.getStartDate())
                .createdAt(timeHelper.getCurrentMilliSecond())
                .build();


    }
}

package yeter.ugur.insuranceexample.service.mapper;

import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.service.PolicyPremiumHelper;

import java.time.LocalDate;
import java.util.List;

public final class PolicyCreationResponseDtoMapper {

    private PolicyCreationResponseDtoMapper() {

    }

    public static PolicyCreationResponseDto toPolicyCreationResponseDto(List<InsuredPersonEntity> insuredPersons,
                                                                        String externalId,
                                                                        LocalDate startDate) {
        return PolicyCreationResponseDto.builder()
                .policyId(externalId)
                .startDate(startDate)
                .insuredPersons(InsuredPersonMapper.toInsuredPersonsDto(insuredPersons))
                .totalPremium(PolicyPremiumHelper.calculateTotalPremium(insuredPersons))
                .build();
    }

}

package yeter.ugur.insuranceexample.service.mapper;

import org.junit.jupiter.api.Test;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.service.PolicyPremiumHelper;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static yeter.ugur.insuranceexample.TestHelper.EXTERNAL_POLICY_ID;
import static yeter.ugur.insuranceexample.TestHelper.START_DATE;
import static yeter.ugur.insuranceexample.TestHelper.getInsuredPersonEntities;
import static yeter.ugur.insuranceexample.service.mapper.PolicyCreationResponseDtoMapper.toPolicyCreationResponseDto;

class PolicyCreationResponseDtoMapperTest {

    @Test
    void itMapsPolicyCreationResponseDto() {
        List<InsuredPersonEntity> insuredPersonEntities = getInsuredPersonEntities();
        PolicyCreationResponseDto policyCreationResponseDto = toPolicyCreationResponseDto(
                insuredPersonEntities,
                EXTERNAL_POLICY_ID,
                START_DATE);

        assertThat(policyCreationResponseDto.getPolicyId()).isEqualTo(EXTERNAL_POLICY_ID);
        assertThat(policyCreationResponseDto.getStartDate()).isEqualTo(START_DATE);
        assertThat(policyCreationResponseDto.getTotalPremium())
                .isEqualTo(PolicyPremiumHelper.calculateTotalPremium(insuredPersonEntities));
        assertThat(policyCreationResponseDto.getInsuredPersons())
                .isEqualTo(InsuredPersonMapper.toInsuredPersonsDto(insuredPersonEntities));
    }
}

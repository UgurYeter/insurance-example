package yeter.ugur.insuranceexample.service.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationResponseDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.helper.PolicyTestDataHelper;
import yeter.ugur.insuranceexample.service.helper.PolicyPremiumHelper;
import yeter.ugur.insuranceexample.service.helper.TimeHelper;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static yeter.ugur.insuranceexample.helper.InsuredPersonEntityTestHelper.getInsuredPersonEntities;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.EXTERNAL_POLICY_ID;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.NOW_IN_MILLI;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.START_DATE;

@ExtendWith(MockitoExtension.class)
class PolicyObjectsMapperTest {

    @Mock
    private TimeHelper timeHelper;
    @Mock
    private InsuredPersonMapper insuredPersonMapper;
    @InjectMocks
    private PolicyObjectsMapper policyObjectsMapper;

    @Test
    void itMapsPolicyCreationResponseDto() {
        List<InsuredPersonEntity> insuredPersonEntities = getInsuredPersonEntities();

        PolicyCreationResponseDto policyCreationResponseDto = policyObjectsMapper.toPolicyCreationResponseDto(
                insuredPersonEntities,
                EXTERNAL_POLICY_ID,
                START_DATE);

        assertThat(policyCreationResponseDto.getPolicyId()).isEqualTo(EXTERNAL_POLICY_ID);
        assertThat(policyCreationResponseDto.getStartDate()).isEqualTo(START_DATE);
        assertThat(policyCreationResponseDto.getTotalPremium())
                .isEqualTo(PolicyPremiumHelper.calculateTotalPremium(insuredPersonEntities));
        verify(insuredPersonMapper).toInsuredPersonsDto(insuredPersonEntities);
    }

    @Test
    void itMapsToPolicyEntityWithoutInsuredPersons() {
        when(timeHelper.getCurrentMilliSecond()).thenReturn(NOW_IN_MILLI);
        PolicyCreationRequestDto creationRequestDto = PolicyTestDataHelper.prototypeRequestWithInsuredPersons();

        PolicyEntity policyCreationResponseDto = policyObjectsMapper
                .mapToPolicyEntityWithoutInsuredPersons(creationRequestDto,
                        EXTERNAL_POLICY_ID
                );

        assertThat(policyCreationResponseDto.getExternalId()).isEqualTo(EXTERNAL_POLICY_ID);
        assertThat(policyCreationResponseDto.getStartDate()).isEqualTo(creationRequestDto.getStartDate());
        assertThat(policyCreationResponseDto.getCreatedAt()).isEqualTo(NOW_IN_MILLI);
    }
}

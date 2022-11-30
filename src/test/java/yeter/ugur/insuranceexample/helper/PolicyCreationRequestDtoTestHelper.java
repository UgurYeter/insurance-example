package yeter.ugur.insuranceexample.helper;

import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.api.creation.PolicyCreationRequestDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.FIRST_NAME_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.FIRST_NAME_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PREMIUM_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.SECOND_NAME_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.SECOND_NAME_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.START_DATE;

public final class PolicyCreationRequestDtoTestHelper {

    private PolicyCreationRequestDtoTestHelper() {

    }

    public static PolicyCreationRequestDto prototypeRequestWithInsuredPersons() {
        PolicyCreationRequestDto policyCreationRequestDto = prototypeRequestWithOneInsuredPerson();
        List<InsuredPersonDto> insuredPersons = new ArrayList<>(policyCreationRequestDto.getInsuredPersons());
        insuredPersons.add(InsuredPersonDto.builder()
                .firstName(FIRST_NAME_2)
                .secondName(SECOND_NAME_2)
                .premium(PREMIUM_2)
                .build());
        policyCreationRequestDto.setInsuredPersons(insuredPersons);
        return policyCreationRequestDto;
    }

    public static PolicyCreationRequestDto prototypeRequestWithOneInsuredPerson() {
        PolicyCreationRequestDto policyCreationRequestDto = new PolicyCreationRequestDto();
        policyCreationRequestDto.setStartDate(START_DATE);
        policyCreationRequestDto.setInsuredPersons(List.of(InsuredPersonDto.builder()
                .firstName(FIRST_NAME_1)
                .secondName(SECOND_NAME_1)
                .premium(BigDecimal.ONE)
                .build()));
        return policyCreationRequestDto;
    }


}

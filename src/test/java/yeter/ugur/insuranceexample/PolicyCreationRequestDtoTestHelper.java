package yeter.ugur.insuranceexample;

import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.api.PolicyCreationRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class PolicyCreationRequestDtoTestHelper {

    private PolicyCreationRequestDtoTestHelper() {

    }

    public static PolicyCreationRequestDto prototype() {
        PolicyCreationRequestDto policyCreationRequestDto = new PolicyCreationRequestDto();
        policyCreationRequestDto.setStartDate(LocalDate.MIN);
        policyCreationRequestDto.setInsuredPersons(List.of(InsuredPersonDto.builder()
                        .firstName("First-name-1")
                        .secondName("Second-name-1")
                        .premium(BigDecimal.ONE)
                        .build(),
                InsuredPersonDto.builder()
                        .firstName("First-name-2")
                        .secondName("Second-name-2")
                        .premium(BigDecimal.valueOf(2l))
                        .build()
        ));
        return policyCreationRequestDto;
    }
}

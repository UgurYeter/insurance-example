package yeter.ugur.insuranceexample;

import yeter.ugur.insuranceexample.api.InsuredPerson;
import yeter.ugur.insuranceexample.api.PolicyCreationRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class PolicyCreationRequestDtoTestHelper {

    private PolicyCreationRequestDtoTestHelper() {

    }

    public static PolicyCreationRequestDto.PolicyCreationRequestDtoBuilder prototype() {
        return PolicyCreationRequestDto.builder().startDate(LocalDate.MIN)
                .insuredPersons(List.of(InsuredPerson.builder()
                                .firstName("First-name-1")
                                .secondName("Second-name-1")
                                .premium(BigDecimal.ONE)
                                .build(),
                        InsuredPerson.builder()
                                .firstName("First-name-2")
                                .secondName("Second-name-2")
                                .premium(BigDecimal.valueOf(2l))
                                .build()
                ));
    }
}

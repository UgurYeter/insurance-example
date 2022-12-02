package yeter.ugur.insuranceexample.helper;

import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.math.BigDecimal;
import java.util.List;

import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.FIRST_NAME_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.FIRST_NAME_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PERSON_ID_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PERSON_ID_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PREMIUM_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PREMIUM_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.SECOND_NAME_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.SECOND_NAME_2;

public final class InsuredPersonTestHelper {

    private InsuredPersonTestHelper() {

    }

    public static List<InsuredPersonEntity> prototypeInsuredPersonEntities() {
        return List.of(InsuredPersonEntity.builder()
                        .id(PERSON_ID_1)
                        .firstName(FIRST_NAME_1)
                        .secondName(SECOND_NAME_1)
                        .premium(PREMIUM_1)
                        .build(),
                InsuredPersonEntity.builder()
                        .id(PERSON_ID_2)
                        .firstName(FIRST_NAME_2)
                        .secondName(SECOND_NAME_2)
                        .premium(PREMIUM_2)
                        .build());
    }
    public static List<InsuredPersonDto> prototypeInsuredPersonDto(){
        return List.of(InsuredPersonDto.builder()
                .firstName(FIRST_NAME_1)
                .secondName(SECOND_NAME_1)
                .premium(BigDecimal.ONE)
                .build());
    }
}

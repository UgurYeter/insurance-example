package yeter.ugur.insuranceexample.helper;

import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.util.List;

import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.FIRST_NAME_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.FIRST_NAME_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PERSON_ID_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PERSON_ID_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PREMIUM_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PREMIUM_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.SECOND_NAME_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.SECOND_NAME_2;

public final class InsuredPersonEntityTestHelper {

    private InsuredPersonEntityTestHelper() {

    }

    public static List<InsuredPersonEntity> getInsuredPersonEntities() {
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
}

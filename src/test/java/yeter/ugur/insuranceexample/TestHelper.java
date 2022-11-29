package yeter.ugur.insuranceexample;

import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class TestHelper {

    public static final int PERSON_ID_1 = 1;
    public static final String FIRST_NAME_1 = "first-name";
    public static final String SECOND_NAME_1 = "second-name";
    public static final BigDecimal PREMIUM_1 = BigDecimal.valueOf(12.40);
    public static final int PERSON_ID_2 = 2;
    public static final String FIRST_NAME_2 = "first-name-2";
    public static final String SECOND_NAME_2 = "second-name-2";
    public static final BigDecimal PREMIUM_2 = BigDecimal.valueOf(20.00);
    public static final LocalDate START_DATE = LocalDate.of(2022, 12, 12);
    public static final long NOW_IN_MILLI = Instant.now().toEpochMilli();

    public static final String EXTERNAL_POLICY_ID = "CU423DF89";

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

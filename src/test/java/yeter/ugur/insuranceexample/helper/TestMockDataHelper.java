package yeter.ugur.insuranceexample.helper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class TestMockDataHelper {

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
    public static final String EXTERNAL_POLICY_ID = "77002a2e-8294-4402-8d4e-86e2bbcf77ec";

    public static final int POLICY_ID_1 = 1;


}

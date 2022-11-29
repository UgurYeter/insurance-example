package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PolicyPremiumHelperTest {

    private static final BigDecimal PREMIUM_1 = BigDecimal.valueOf(12.10);
    private static final BigDecimal PREMIUM_2 = BigDecimal.valueOf(5.59);

    @ParameterizedTest
    @MethodSource("supplyPremiums")
    void calculateTotalPremium(List<InsuredPersonEntity> insuredPersons, BigDecimal expected) {
        BigDecimal totalPremium = PolicyPremiumHelper.calculateTotalPremium(insuredPersons);

        assertThat(totalPremium).isEqualTo(expected);
    }

    public static Stream<Arguments> supplyPremiums() {
        return Stream.of(
                Arguments.of(
                        null,
                        BigDecimal.ZERO),
                Arguments.of(
                        List.of(),
                        BigDecimal.ZERO),
                Arguments.of(
                        List.of(InsuredPersonEntity.builder()
                                .premium(BigDecimal.ONE)
                                .build()),
                        BigDecimal.ONE),
                Arguments.of(
                        List.of(InsuredPersonEntity.builder()
                                        .premium(PREMIUM_1)
                                        .build(),
                                InsuredPersonEntity.builder()
                                        .premium(PREMIUM_2)
                                        .build()),
                        BigDecimal.valueOf(17.69)),
                Arguments.of(
                        List.of(InsuredPersonEntity.builder()
                                        .premium(PREMIUM_1)
                                        .build(),
                                InsuredPersonEntity.builder()
                                        .premium(PREMIUM_2)
                                        .build(),
                                InsuredPersonEntity.builder()
                                        .premium(BigDecimal.valueOf(10))
                                        .build()),
                        BigDecimal.valueOf(27.69))

        );
    }

}

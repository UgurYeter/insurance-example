package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class InsuredPersonMapperTest {

    private static final int PERSON_ID_1 = 1;
    private static final String FIRST_NAME_1 = "first-name";
    private static final String SECOND_NAME_1 = "second-name";
    private static final BigDecimal PREMIUM_1 = BigDecimal.valueOf(12.40);
    private static final int PERSON_ID_2 = 2;
    private static final String FIRST_NAME_2 = "first-name-2";
    private static final String SECOND_NAME_2 = "second-name-2";
    private static final BigDecimal PREMIUM_2 = BigDecimal.valueOf(20.00);

    @Test
    void itMapsToInsuredPersonEntity() {
        InsuredPersonDto insuredPersonDto = InsuredPersonDto.builder()
                .id(PERSON_ID_1)
                .firstName(FIRST_NAME_1)
                .secondName(SECOND_NAME_1)
                .premium(PREMIUM_1)
                .build();

        InsuredPersonEntity insuredPersonEntity = InsuredPersonMapper.toInsuredPersonEntity(insuredPersonDto);

        assertThat(insuredPersonEntity.getId()).isEqualTo(PERSON_ID_1);
        assertThat(insuredPersonEntity.getFirstName()).isEqualTo(FIRST_NAME_1);
        assertThat(insuredPersonEntity.getSecondName()).isEqualTo(SECOND_NAME_1);
        assertThat(insuredPersonEntity.getPremium()).isEqualTo(PREMIUM_1);
    }

    @Test
    void itMapsToInsuredPersonEntities() {
        List<InsuredPersonDto> insuredPersonDtos = List.of(
                InsuredPersonDto.builder()
                        .id(PERSON_ID_1)
                        .firstName(FIRST_NAME_1)
                        .secondName(SECOND_NAME_1)
                        .premium(PREMIUM_1)
                        .build(),
                InsuredPersonDto.builder()
                        .id(PERSON_ID_2)
                        .firstName(FIRST_NAME_2)
                        .secondName(SECOND_NAME_2)
                        .premium(PREMIUM_2)
                        .build()
        );

        List<InsuredPersonEntity> insuredPersonEntities = InsuredPersonMapper.toInsuredPersonEntities(insuredPersonDtos);
        insuredPersonEntities.sort(Comparator.comparingInt(InsuredPersonEntity::getId));

        InsuredPersonEntity insuredPersonEntityOne = insuredPersonEntities.get(0);
        assertThat(insuredPersonEntityOne.getId()).isEqualTo(PERSON_ID_1);
        assertThat(insuredPersonEntityOne.getFirstName()).isEqualTo(FIRST_NAME_1);
        assertThat(insuredPersonEntityOne.getSecondName()).isEqualTo(SECOND_NAME_1);
        assertThat(insuredPersonEntityOne.getPremium()).isEqualTo(PREMIUM_1);

        InsuredPersonEntity insuredPersonEntityTwo = insuredPersonEntities.get(1);
        assertThat(insuredPersonEntityTwo.getId()).isEqualTo(PERSON_ID_2);
        assertThat(insuredPersonEntityTwo.getFirstName()).isEqualTo(FIRST_NAME_2);
        assertThat(insuredPersonEntityTwo.getSecondName()).isEqualTo(SECOND_NAME_2);
        assertThat(insuredPersonEntityTwo.getPremium()).isEqualTo(PREMIUM_2);
    }
}

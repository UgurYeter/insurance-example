package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.FIRST_NAME_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.FIRST_NAME_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PERSON_ID_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PERSON_ID_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PREMIUM_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.PREMIUM_2;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.SECOND_NAME_1;
import static yeter.ugur.insuranceexample.helper.TestMockDataHelper.SECOND_NAME_2;

class InsuredPersonMapperTest {

    private final InsuredPersonMapper insuredPersonMapper = new InsuredPersonMapper();
    @Test
    void itMapsToInsuredPersonEntity() {
        List<InsuredPersonDto> insuredPersonDtos = List.of(InsuredPersonDto.builder()
                .id(PERSON_ID_1)
                .firstName(FIRST_NAME_1)
                .secondName(SECOND_NAME_1)
                .premium(PREMIUM_1)
                .build());

        InsuredPersonEntity insuredPersonEntity = insuredPersonMapper.toInsuredPersonEntities(insuredPersonDtos).get(0);

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

        List<InsuredPersonEntity> insuredPersonEntities = insuredPersonMapper.toInsuredPersonEntities(insuredPersonDtos);
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

    @Test
    void itMapsToInsuredPersonsDto() {
        List<InsuredPersonEntity> insuredPersons = List.of(
                InsuredPersonEntity.builder()
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
                        .build()

        );

        List<InsuredPersonDto> insuredPersonDtos = insuredPersonMapper.toInsuredPersonsDto(insuredPersons);

        insuredPersonDtos.sort(Comparator.comparingInt(InsuredPersonDto::getId));
        InsuredPersonEntity insuredPersonEntityOne = insuredPersons.get(0);
        assertThat(insuredPersonEntityOne.getId()).isEqualTo(PERSON_ID_1);
        assertThat(insuredPersonEntityOne.getFirstName()).isEqualTo(FIRST_NAME_1);
        assertThat(insuredPersonEntityOne.getSecondName()).isEqualTo(SECOND_NAME_1);
        assertThat(insuredPersonEntityOne.getPremium()).isEqualTo(PREMIUM_1);
        InsuredPersonEntity insuredPersonEntityTwo = insuredPersons.get(1);

        assertThat(insuredPersonEntityTwo.getId()).isEqualTo(PERSON_ID_2);
        assertThat(insuredPersonEntityTwo.getFirstName()).isEqualTo(FIRST_NAME_2);
        assertThat(insuredPersonEntityTwo.getSecondName()).isEqualTo(SECOND_NAME_2);
        assertThat(insuredPersonEntityTwo.getPremium()).isEqualTo(PREMIUM_2);
    }
}

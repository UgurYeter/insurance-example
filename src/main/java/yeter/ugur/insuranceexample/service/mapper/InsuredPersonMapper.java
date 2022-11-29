package yeter.ugur.insuranceexample.service.mapper;

import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class InsuredPersonMapper {

    private InsuredPersonMapper() {

    }

    public static List<InsuredPersonEntity> toInsuredPersonEntities(List<InsuredPersonDto> insuredPersons) {
        return insuredPersons
                .stream()
                .map(InsuredPersonMapper::toInsuredPersonEntity)
                .collect(Collectors.toList());
    }

    private static InsuredPersonEntity toInsuredPersonEntity(InsuredPersonDto person) {
        return InsuredPersonEntity.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .secondName(person.getSecondName())
                .premium(person.getPremium())
                .build();
    }

    public static List<InsuredPersonDto> toInsuredPersonsDto(List<InsuredPersonEntity> insuredPersons) {
        return insuredPersons.stream()
                .map(insuredPersonEntity -> InsuredPersonDto.builder()
                        .id(insuredPersonEntity.getId())
                        .firstName(insuredPersonEntity.getFirstName())
                        .secondName(insuredPersonEntity.getSecondName())
                        .premium(insuredPersonEntity.getPremium())
                        .build())
                .collect(Collectors.toList());
    }
}

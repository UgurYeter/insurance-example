package yeter.ugur.insuranceexample.service;

import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;

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

    static InsuredPersonEntity toInsuredPersonEntity(InsuredPersonDto person) {
        return InsuredPersonEntity.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .secondName(person.getSecondName())
                .premium(person.getPremium())
                .build();
    }

    public static List<InsuredPersonDto> toInsuredPersonsDto(PolicyEntity newPolicyState) {
        return newPolicyState.getInsuredPersons().stream()
                .map(insuredPersonEntity -> InsuredPersonDto.builder()
                        .id(insuredPersonEntity.getId())
                        .firstName(insuredPersonEntity.getFirstName())
                        .secondName(insuredPersonEntity.getSecondName())
                        .premium(insuredPersonEntity.getPremium())
                        .build())
                .collect(Collectors.toList());
    }
}

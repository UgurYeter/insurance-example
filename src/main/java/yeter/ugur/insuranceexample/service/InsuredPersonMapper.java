package yeter.ugur.insuranceexample.service;

import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class InsuredPersonMapper {

    private InsuredPersonMapper() {

    }

    public static List<InsuredPersonEntity> mapToPersonEntities(List<InsuredPersonDto> insuredPersons) {
        return insuredPersons
                .stream()
                .map(person -> InsuredPersonEntity.builder()
                        .firstName(person.getFirstName())
                        .secondName(person.getSecondName())
                        .premium(person.getPremium())
                        .build())
                .collect(Collectors.toList());
    }

    public static List<Integer> collectPersonIds(List<InsuredPersonDto> insuredPersons) {
        return insuredPersons
                .stream()
                .map(InsuredPersonDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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

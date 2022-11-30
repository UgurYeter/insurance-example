package yeter.ugur.insuranceexample.service.mapper;

import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.api.InsuredPersonDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InsuredPersonMapper {

    public List<InsuredPersonEntity> toInsuredPersonEntities(List<InsuredPersonDto> insuredPersons) {
        return insuredPersons
                .stream()
                .map(this::toInsuredPersonEntity)
                .collect(Collectors.toList());
    }

    private InsuredPersonEntity toInsuredPersonEntity(InsuredPersonDto person) {
        return InsuredPersonEntity.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .secondName(person.getSecondName())
                .premium(person.getPremium())
                .build();
    }

    public List<InsuredPersonDto> toInsuredPersonsDto(List<InsuredPersonEntity> insuredPersons) {
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

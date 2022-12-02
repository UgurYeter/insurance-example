package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.api.PolicyIsNotFoundException;
import yeter.ugur.insuranceexample.api.modification.CollidingPolicyEffectiveDate;
import yeter.ugur.insuranceexample.api.modification.PolicyModificationRequestDto;
import yeter.ugur.insuranceexample.dao.InsuredPersonEntity;
import yeter.ugur.insuranceexample.dao.PolicyEntity;
import yeter.ugur.insuranceexample.helper.InsuredPersonTestHelper;
import yeter.ugur.insuranceexample.helper.PolicyTestDataHelper;
import yeter.ugur.insuranceexample.service.helper.PolicyStateHelper;
import yeter.ugur.insuranceexample.service.mapper.InsuredPersonMapper;
import yeter.ugur.insuranceexample.service.mapper.PolicyObjectsMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyModificationServiceTest {

    @Mock
    private PolicyAndInsuredPersonStorageHelper policyAndInsuredPersonStorageHelper;
    @Mock
    private PolicyStateHelper policyStateHelper;
    @Mock
    private PolicyObjectsMapper policyObjectsMapper;
    @Mock
    private InsuredPersonMapper insuredPersonMapper;

    @InjectMocks
    private PolicyModificationService policyModificationService;

    @Test
    void itThrowsExceptionWhenStateIsNotUnique() {
        PolicyModificationRequestDto policyModificationRequestDto = PolicyTestDataHelper.prototypeModificationRequestionWithoutInsuredPersons();
        when(policyStateHelper.findPolicyStateByExternalIdAndStartDate(
                policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate()
        )).thenReturn(Optional.of(PolicyTestDataHelper.prototypePolicyEntity().build()));

        assertThrows(CollidingPolicyEffectiveDate.class,
                () -> policyModificationService.modifyPolicy(policyModificationRequestDto));
    }

    @Test
    void itThrowsExceptionWhenNoPriorPolicyStateFound() {
        PolicyModificationRequestDto policyModificationRequestDto = PolicyTestDataHelper.prototypeModificationRequestionWithoutInsuredPersons();
        when(policyStateHelper.findPolicyStateByExternalIdAndStartDate(
                policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate()
        )).thenReturn(Optional.empty());
        when(policyStateHelper.findLatestPolicyStatePriorToDate(
                policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate()
        )).thenReturn(Optional.empty());

        assertThrows(PolicyIsNotFoundException.class,
                () -> policyModificationService.modifyPolicy(policyModificationRequestDto));
    }

    @Test
    void itModifiesPolicyByCreatingNewState() {
        PolicyModificationRequestDto policyModificationRequestDto = PolicyTestDataHelper.prototypeModificationRequestionWithoutInsuredPersons();
        when(policyStateHelper.findPolicyStateByExternalIdAndStartDate(
                policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate()
        )).thenReturn(Optional.empty());
        when(policyStateHelper.findLatestPolicyStatePriorToDate(policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate()))
                .thenReturn(Optional.of(PolicyTestDataHelper.prototypePolicyEntity().build()));
        List<InsuredPersonEntity> insuredPersonEntities = InsuredPersonTestHelper.prototypeInsuredPersonEntities();
        when(insuredPersonMapper.toInsuredPersonEntities(policyModificationRequestDto.getInsuredPersons())).thenReturn(
                insuredPersonEntities
        );
        PolicyEntity newPolicyState = PolicyTestDataHelper.prototypePolicyEntity().build();
        when(policyObjectsMapper.mapToPolicyEntityWithoutInsuredPersons(
                policyModificationRequestDto.getPolicyId(),
                policyModificationRequestDto.getEffectiveDate()))
                .thenReturn(newPolicyState);
        when(policyAndInsuredPersonStorageHelper.createPolicyWithInsuredPersons(
                newPolicyState,
                insuredPersonEntities

        )).thenReturn(newPolicyState);

        policyModificationService.modifyPolicy(policyModificationRequestDto);

        verify(policyObjectsMapper).toPolicyModificationResponseDto(newPolicyState);
    }
}

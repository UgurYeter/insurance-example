package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyManagerTest {

    private static final String EXTERNAL_POLICY_ID = "CU423DF89";

    @Mock
    private ExternalPolicyIdGenerator externalPolicyIdGenerator;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PolicyManager policyManager;

    @Test
    void itChecksIfPolicyIdUnique() {
        when(externalPolicyIdGenerator.generate()).thenReturn(EXTERNAL_POLICY_ID);
        when(policyRepository.findByExternalId(EXTERNAL_POLICY_ID)).thenReturn(Optional.empty());

        String uniqueExternalPolicyId = policyManager.getUniqueExternalPolicyId();

        verify(policyRepository).findByExternalId(EXTERNAL_POLICY_ID);
        assertThat(uniqueExternalPolicyId).isEqualTo(EXTERNAL_POLICY_ID);
    }


}

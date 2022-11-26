package yeter.ugur.insuranceexample.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static yeter.ugur.insuranceexample.service.ExternalPolicyIdCreator.EXTERNAL_POLICY_ID_LENGTH;

@ExtendWith(MockitoExtension.class)
class ExternalPolicyIdCreatorTest {

    private static final Pattern EXTERNAL_POLICY_ID_PATTERN = Pattern.compile("^[A-Z0-9]"
            + "{" + EXTERNAL_POLICY_ID_LENGTH + "}"
            + "$");
    private ExternalPolicyIdCreator externalPolicyIdCreator = new ExternalPolicyIdCreator();

    @Test
    void itGeneratesAlphanumericStringWithExpectedLength() {
        String externalPolicyId = externalPolicyIdCreator.generateExternalPolicyId();

        assertThat(externalPolicyId).matches(EXTERNAL_POLICY_ID_PATTERN);
    }
}

package yeter.ugur.insuranceexample.service;

import org.assertj.core.util.VisibleForTesting;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
class ExternalPolicyIdGenerator {

    /**
     * Considering the example from the task description ("policyId": "CU423DF89"),
     * assumption here is that the policy id is consist of 9 alphanumeric character with capital letters.
     */
    @VisibleForTesting
    static final int EXTERNAL_POLICY_ID_LENGTH = 9;

    private static final char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    String generate() {
        Random random = new SecureRandom();
        char[] result = new char[EXTERNAL_POLICY_ID_LENGTH];
        for (int i = 0; i < result.length; i++) {
            // picks a random index out of character set > random character
            int randomCharIndex = random.nextInt(CHARSET_AZ_09.length);
            result[i] = CHARSET_AZ_09[randomCharIndex];
        }
        return new String(result);
    }
}

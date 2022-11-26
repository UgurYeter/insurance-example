package yeter.ugur.insuranceexample.service;

import org.springframework.stereotype.Component;
import yeter.ugur.insuranceexample.dao.PolicyRepository;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Component
public class ExternalPolicyIdCreator {

  private static final int ID_LENGTH = 9;
  private static final char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
  private static final int MAX_ID_GENERATION_ATTEMPT = 5;

  private final PolicyRepository policyRepository;

  public ExternalPolicyIdCreator(PolicyRepository policyRepository) {
    this.policyRepository = policyRepository;
  }

  public Optional<String> generatePolicyId() {
    int attempt = 0;
    boolean existingId;
    Optional<String> generatedId;
    do {
      String candidate = generateRandomString();
      existingId = policyRepository.findByExternalId(candidate).isPresent();
      attempt++;
      generatedId = Optional.of(candidate);
    } while (existingId && attempt < MAX_ID_GENERATION_ATTEMPT);

    return generatedId;
  }

  private String generateRandomString() {
    Random random = new SecureRandom();
    char[] result = new char[ID_LENGTH];
    for (int i = 0; i < result.length; i++) {
      // picks a random index out of character set > random character
      int randomCharIndex = random.nextInt(CHARSET_AZ_09.length);
      result[i] = CHARSET_AZ_09[randomCharIndex];
    }
    return new String(result);
  }
}

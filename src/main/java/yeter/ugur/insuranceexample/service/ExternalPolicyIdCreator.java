package yeter.ugur.insuranceexample.service;

import java.security.SecureRandom;
import java.util.Random;

public class ExternalPolicyIdCreator {

  private static final int ID_LENGTH = 9;

  private static final char[] CHARSET_AZ_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

  public String generatePolicyId() {
    String candidate = generateRandomString();
    // TODO check if it exists

    return candidate;
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

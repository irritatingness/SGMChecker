package net.sourceforge.queried.util;

public class Validator {

  private Validator() throws IllegalAccessException {
    throw new IllegalAccessException("Must not be instantiated");
  }

  public static void notNullOrEmpty(String string, String message) {
    if (StringUtils.isNullOrEmpty(string)) {
      throw new IllegalArgumentException(message);
    }
  }
}

package net.sourceforge.queried.util;

public final class StringUtils {

  private StringUtils() throws IllegalAccessException {
    throw new IllegalAccessException("Must not be instantiated");
  }

  public static boolean isNullOrEmpty(String string) {
    return string == null || string.length() < 1;
  }
}

package dev.aulait.svqk.arch.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectStringUtils {

  public static boolean isNotEmpty(Object value) {
    return value != null && !value.toString().isEmpty();
  }
}

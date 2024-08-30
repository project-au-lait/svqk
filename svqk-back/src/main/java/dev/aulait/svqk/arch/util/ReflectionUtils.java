package dev.aulait.svqk.arch.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {

  public static <T> T newInstance(Class<T> type) {
    try {
      return type.getConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchMethodException
        | SecurityException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> T invokeMethod(Object obj, Method method, Class<T> returnType, Object... args) {
    try {
      return returnType.cast(method.invoke(obj, args));
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new IllegalArgumentException(e);
    }
  }
}

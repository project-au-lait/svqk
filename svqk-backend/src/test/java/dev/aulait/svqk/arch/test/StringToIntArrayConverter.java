package dev.aulait.svqk.arch.test;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

public class StringToIntArrayConverter extends TypedArgumentConverter<String, int[]> {

  protected StringToIntArrayConverter() {
    super(String.class, int[].class);
  }

  @Override
  protected int[] convert(String source) throws ArgumentConversionException {
    if (StringUtils.isEmpty(source)) {
      return new int[0];
    }
    return Arrays.stream(source.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
  }
}

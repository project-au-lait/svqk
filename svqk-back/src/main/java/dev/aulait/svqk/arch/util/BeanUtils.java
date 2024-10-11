package dev.aulait.svqk.arch.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtils {

  private static ModelMapper mapper = new ModelMapper();

  static {
    mapper.addConverter(
        new AbstractConverter<String, LocalDate>() {
          protected LocalDate convert(String source) {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          }
        });
  }

  public static <T> T map(Object src, Class<T> dstType) {
    return mapper.map(src, dstType);
  }

  public static <S, D> List<D> mapAll(Collection<S> srcs, Class<D> dstType) {
    if (srcs == null) {
      return Collections.emptyList();
    }
    return srcs.stream().map(src -> mapper.map(src, dstType)).toList();
  }
}

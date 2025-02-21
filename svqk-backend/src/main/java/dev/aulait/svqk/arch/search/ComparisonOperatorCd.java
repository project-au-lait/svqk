package dev.aulait.svqk.arch.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum ComparisonOperatorCd {
  EQ("="),
  NE("<>"),
  GT(">"),
  GE(">="),
  LT("<"),
  LE("<="),
  LIKE("LIKE"),
  IN("IN");

  @Getter private String value;

  public static ComparisonOperatorCd parse(String operator) {
    for (ComparisonOperatorCd cd : values()) {
      if (StringUtils.equalsIgnoreCase(cd.name(), operator)) {
        return cd;
      }
    }

    throw new IllegalArgumentException(
        operator + " is not parsed to " + ComparisonOperatorCd.class.getSimpleName());
  }
}

package dev.aulait.svqk.arch.search;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum OperatorCd {
  EQ("="),
  NE("<>"),
  GT(">"),
  GE(">="),
  LT("<"),
  LE("<="),
  LIKE("LIKE"),
  IN("IN");

  @Getter
  private String value;

  public static OperatorCd parse(String operator) {
    for (OperatorCd cd : values()) {
      if (StringUtils.equalsIgnoreCase(cd.name(), operator)) {
        return cd;
      }
    }

    throw new IllegalArgumentException(operator + " is not parsed to " + OperatorCd.class.getSimpleName());
  }
}

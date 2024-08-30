package dev.aulait.svqk.arch.search;

import java.util.Collection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FieldConditionVo {
  private String field;
  private OperatorCd operator;
  private ConjunctionCd conjunction;
  private Object value;

  public static FieldConditionVo of(String field, Object value) {
    return of(field, OperatorCd.EQ, value);
  }

  public static FieldConditionVo of(String field, OperatorCd operator, Object value) {
    return of(field, operator, ConjunctionCd.AND, value);
  }

  public static FieldConditionVo of(String field, OperatorCd operator, ConjunctionCd conjunction, Object value) {
    return FieldConditionVo.builder().field(field).operator(operator).value(value)
        .conjunction(conjunction).build();
  }

  public boolean hasValue() {
    if (value == null) {
      return false;
    }

    if (value.toString().isEmpty()) {
      return false;
    }

    if (value instanceof Collection) {
      return !Collection.class.cast(value).isEmpty();
    }

    return true;
  }

}

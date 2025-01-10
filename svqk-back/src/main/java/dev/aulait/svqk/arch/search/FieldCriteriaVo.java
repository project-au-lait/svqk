package dev.aulait.svqk.arch.search;

import java.util.Collection;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FieldCriteriaVo {
  private String field;
  private ComparisonOperatorCd comparisonOperator;
  private LogicalOperatorCd logicalOperator;
  private Object value;

  public static FieldCriteriaVo of(String field, Object value) {
    return of(field, ComparisonOperatorCd.EQ, value);
  }

  public static FieldCriteriaVo of(String field, ComparisonOperatorCd operator, Object value) {
    return of(field, operator, LogicalOperatorCd.AND, value);
  }

  public static FieldCriteriaVo of(
      String field,
      ComparisonOperatorCd comparisonOperator,
      LogicalOperatorCd logicalOperator,
      Object value) {
    return FieldCriteriaVo.builder()
        .field(field)
        .comparisonOperator(comparisonOperator)
        .value(value)
        .logicalOperator(logicalOperator)
        .build();
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

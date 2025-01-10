package dev.aulait.svqk.arch.search;

import java.util.Collection;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FieldCriteriaVo {
  private String field;
  @Builder.Default private ComparisonOperatorCd comparisonOperator = ComparisonOperatorCd.EQ;
  @Builder.Default private LogicalOperatorCd logicalOperator = LogicalOperatorCd.AND;
  private Object value;

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

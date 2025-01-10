package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaBuilder {

  private StringBuilder select = new StringBuilder();
  private List<FieldCriteriaVo> fields = new ArrayList<>();
  private SortOrderDto defaultOrder;

  public SearchCriteriaBuilder select(String select) {
    if (this.select.length() > 0) {
      this.select.append(" ");
    }
    this.select.append(select);
    return this;
  }

  public SearchCriteriaBuilder where(String field, Object value) {
    return where(field, ComparisonOperatorCd.EQ, value);
  }

  public SearchCriteriaBuilder where(
      String field, ComparisonOperatorCd comparisonOperator, Object value) {
    return where(LogicalOperatorCd.AND, field, comparisonOperator, value);
  }

  public SearchCriteriaBuilder where(
      LogicalOperatorCd logicalOperator,
      String field,
      ComparisonOperatorCd comparisonOperator,
      Object value) {

    fields.add(
        FieldCriteriaVo.builder()
            .logicalOperator(logicalOperator)
            .field(field)
            .comparisonOperator(comparisonOperator)
            .value(value)
            .build());

    return this;
  }

  public SearchCriteriaBuilder defaultOrderBy(String field, boolean asc) {
    this.defaultOrder = SortOrderDto.builder().field(field).asc(asc).build();
    return this;
  }

  public SearchCriteriaVo build(PageControlDto pageControl) {
    if (defaultOrder != null && pageControl.getSortOrders().isEmpty()) {
      pageControl.getSortOrders().add(defaultOrder);
    }

    return SearchCriteriaVo.builder()
        .select(select.toString())
        .fieldCriteria(fields)
        .pageControl(pageControl)
        .build();
  }
}

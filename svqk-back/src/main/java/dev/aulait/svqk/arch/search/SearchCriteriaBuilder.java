package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class SearchCriteriaBuilder {

  private StringBuilder select = new StringBuilder();
  private List<FieldCriteriaVo> fields = new ArrayList<>();
  private Order defaultOrder;

  public SearchCriteriaBuilder select(String select) {
    if (this.select.length() > 0) {
      this.select.append(" ");
    }
    this.select.append(select);
    return this;
  }

  public SearchCriteriaBuilder where(String field, Object value) {
    return where(field, ArithmeticOperatorCd.EQ, value);
  }

  public SearchCriteriaBuilder where(
      String field, ArithmeticOperatorCd arithmeticOperator, Object value) {
    return where(LogicalOperatorCd.AND, field, arithmeticOperator, value);
  }

  public SearchCriteriaBuilder where(
      LogicalOperatorCd logicalOperator,
      String field,
      ArithmeticOperatorCd arithmeticOperator,
      Object value) {

    fields.add(
        FieldCriteriaVo.builder()
            .logicalOperator(logicalOperator)
            .field(field)
            .arithmeticOperator(arithmeticOperator)
            .value(value)
            .build());

    return this;
  }

  public SearchCriteriaBuilder defaultOrderBy(String field, boolean asc) {
    this.defaultOrder = new Order(asc ? Direction.ASC : Direction.DESC, field);
    return this;
  }

  public SearchCriteriaVo build(SearchCriteriaDto criteria) {
    return SearchCriteriaVo.builder()
        .select(select.toString())
        .fieldCriteria(fields)
        .pageable(buildPageable(criteria))
        .pageNumsRange(Math.max(criteria.getPageNumsRange(), 2))
        .build();
  }

  private Pageable buildPageable(SearchCriteriaDto criteria) {

    Sort sort = buildSort(criteria.getSortOrders());

    int pageNumberSd = criteria.getPageNumber() - 1;

    return PageRequest.of(
        pageNumberSd < 0 ? 0 : pageNumberSd,
        criteria.getPageSize() < 1 ? 10 : criteria.getPageSize(),
        sort);
  }

  private Sort buildSort(List<SortOrderDto> sortOrders) {
    if (sortOrders.isEmpty()) {

      if (defaultOrder == null) {
        return Sort.unsorted();
      } else {
        return Sort.by(defaultOrder);
      }
    }

    List<Order> orders = sortOrders.stream().map(this::map).toList();
    return Sort.by(orders);
  }

  private Order map(SortOrderDto sortOrder) {
    return new Order(sortOrder.isAsc() ? Direction.ASC : Direction.DESC, sortOrder.getField());
  }
}

package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class SearchConditionBuilder {

  private List<FieldConditionVo> fields = new ArrayList<>();
  private Order defaultOrder;

  public SearchConditionBuilder where(String field, Object value) {
    return this.where(field, OperatorCd.EQ, value);
  }

  public SearchConditionBuilder where(String field, OperatorCd operator, Object value) {
    return where(field, operator, ConjunctionCd.AND, value);
  }

  public SearchConditionBuilder where(
      String field, OperatorCd operator, ConjunctionCd conjunction, Object value) {

    fields.add(
        FieldConditionVo.builder()
            .field(field)
            .operator(operator)
            .value(value)
            .conjunction(conjunction)
            .build());

    return this;
  }

  public SearchConditionBuilder defaultOrderBy(String field, boolean asc) {
    this.defaultOrder = new Order(asc ? Direction.ASC : Direction.DESC, field);
    return this;
  }

  public SearchConditionVo build(SearchConditionDto condition) {
    return SearchConditionVo.of(fields, buildPageable(condition));
  }

  private Pageable buildPageable(SearchConditionDto cond) {

    List<Order> orders = cond.getSortOrders().stream().map(this::map).toList();

    Sort sort = orders.isEmpty() ? Sort.by(defaultOrder) : Sort.by(orders);

    int pageNumberSd = cond.getPageNumber() - 1;

    return PageRequest.of(
        pageNumberSd < 0 ? 0 : pageNumberSd,
        cond.getPageSize() < 1 ? 10 : cond.getPageSize(),
        sort);
  }

  private Order map(SortOrderDto sortOrder) {
    return new Order(sortOrder.isAsc() ? Direction.ASC : Direction.DESC, sortOrder.getField());
  }
}

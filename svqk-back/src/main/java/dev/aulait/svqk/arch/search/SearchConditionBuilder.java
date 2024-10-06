package dev.aulait.svqk.arch.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class SearchConditionBuilder {

  private Class<?> mainEntity;
  private List<JoinVo> joins = new ArrayList<>();
  private List<FieldConditionVo> fields = new ArrayList<>();
  private Order defaultOrder;

  public SearchConditionBuilder from(Class<?> mainEntity) {
    this.mainEntity = mainEntity;
    return this;
  }

  public SearchConditionBuilder join(String rightEntityField) {
    return join(rightEntityField, rightEntityField);
  }

  public SearchConditionBuilder join(String rightEntityField, String alias) {
    return join(null, rightEntityField, alias);
  }

  public SearchConditionBuilder join(String fieldOwner, String field, String alias) {
    joins.add(
        JoinVo.builder()
            .fieldOwner(fieldOwner)
            .field(field)
            .alias(alias)
            .left(false)
            .fetch(true)
            .build());

    return this;
  }

  public SearchConditionBuilder where(String field, Object value) {
    return where(field, OperatorCd.EQ, value);
  }

  public SearchConditionBuilder where(String entityAlias, String field, Object value) {
    return where(ConjunctionCd.AND, entityAlias, field, OperatorCd.EQ, value);
  }

  public SearchConditionBuilder where(String field, OperatorCd operator, Object value) {
    return where(ConjunctionCd.AND, field, operator, value);
  }

  public SearchConditionBuilder where(
      ConjunctionCd conjunction, String field, OperatorCd operator, Object value) {

    return where(conjunction, null, field, operator, value);
  }

  public SearchConditionBuilder where(
      ConjunctionCd conjunction,
      String entityAlias,
      String field,
      OperatorCd operator,
      Object value) {

    fields.add(
        FieldConditionVo.builder()
            .conjunction(conjunction)
            .entityAlias(entityAlias)
            .field(field)
            .operator(operator)
            .value(value)
            .build());

    return this;
  }

  public SearchConditionBuilder defaultOrderBy(String field, boolean asc) {
    this.defaultOrder = new Order(asc ? Direction.ASC : Direction.DESC, field);
    return this;
  }

  public SearchConditionVo build(SearchConditionDto condition) {
    return SearchConditionVo.builder()
        .mainEntity(mainEntity)
        .joins(joins)
        .fieldConditions(fields)
        .pageable(buildPageable(condition))
        .build();
  }

  private Pageable buildPageable(SearchConditionDto cond) {

    Sort sort = buildSort(cond.getSortOrders());

    int pageNumberSd = cond.getPageNumber() - 1;

    return PageRequest.of(
        pageNumberSd < 0 ? 0 : pageNumberSd,
        cond.getPageSize() < 1 ? 10 : cond.getPageSize(),
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

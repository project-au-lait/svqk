package dev.aulait.svqk.arch.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class SearchQueryBuilder {

  @Getter private Map<String, Object> queryParams = new HashMap<>();

  @Getter private String countQuery;

  @Getter private String searchQuery;

  public void buildQuery(SearchCriteriaVo criteria) {
    StringBuilder search = new StringBuilder();
    StringBuilder count = new StringBuilder();

    String select = criteria.getSelect();
    search.append(select);
    count.append(toCountQuery(select));

    String where = buildWhere(criteria.getFieldCriteria());
    if (StringUtils.isNotEmpty(where)) {
      search.append(" WHERE ").append(where);
      count.append(" WHERE ").append(where);
    }

    search.append(buildOrderBy(criteria.getPageControl().getSortOrders()));

    searchQuery = search.toString();
    countQuery = count.toString();
  }

  String toCountQuery(String searchQuery) {
    return searchQuery
        .replaceFirst("SELECT ", "SELECT COUNT(")
        .replaceFirst(" FROM", ") FROM")
        .replace("FETCH ", "");
  }

  String buildWhere(List<FieldCriteriaVo> fieldCriteria) {
    StringBuilder sb = new StringBuilder();

    for (FieldCriteriaVo fieldCriterion : fieldCriteria) {

      if (!fieldCriterion.hasValue()) {
        continue;
      }

      if (sb.length() > 0) {
        sb.append(" " + fieldCriterion.getLogicalOperator() + " ");
      }

      String field = fieldCriterion.getField();
      sb.append(field + " ");

      String paramName = replaceDotsWithUnderscore(field);

      ComparisonOperatorCd operator = fieldCriterion.getComparisonOperator();

      sb.append(operator.getValue() + " :" + paramName);

      queryParams.put(
          paramName,
          operator == ComparisonOperatorCd.LIKE
              ? "%" + fieldCriterion.getValue() + "%"
              : fieldCriterion.getValue());
    }

    return sb.toString();
  }

  String buildOrderBy(List<SortOrderDto> sortOrders) {
    if (sortOrders.isEmpty()) {
      return "";
    }

    return " ORDER BY "
        + sortOrders.stream()
            .map(order -> order.getField() + " " + (order.isAsc() ? "ASC" : "DESC"))
            .collect(Collectors.joining(","));
  }

  private String replaceDotsWithUnderscore(String field) {
    return field.replace(".", "_");
  }
}

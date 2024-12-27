package dev.aulait.svqk.arch.jpa;

import dev.aulait.svqk.arch.search.ArithmeticOperatorCd;
import dev.aulait.svqk.arch.search.FieldCriteriaVo;
import dev.aulait.svqk.arch.search.SearchCriteriaVo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

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

    search.append(buildOrderBy(criteria.getSort()));

    searchQuery = search.toString();
    countQuery = count.toString();
  }

  String toCountQuery(String searchQuery) {
    return searchQuery
        .replaceFirst("SELECT ", "SELECT COUNT(")
        .replaceFirst(" FROM", ") FROM")
        .replaceAll("FETCH ", "");
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

      ArithmeticOperatorCd operator = fieldCriterion.getArithmeticOperator();

      sb.append(operator.getValue() + " :" + paramName);

      queryParams.put(
          paramName,
          operator == ArithmeticOperatorCd.LIKE
              ? "%" + fieldCriterion.getValue() + "%"
              : fieldCriterion.getValue());
    }

    return sb.toString();
  }

  String buildOrderBy(Sort sort) {
    if (sort.isEmpty()) {
      return "";
    }

    return " ORDER BY "
        + sort.get()
            .map(order -> {
                String property = order.getProperty();

                if ("id".equals(property)) {
                    property = "i.id";
                }

                if("updatedAt".equals(property)) {
                    property = "i.updatedAt";
                }

                return property + " " + order.getDirection();
            })
            .collect(Collectors.joining(","));
  }

  private String replaceDotsWithUnderscore(String field) {
    return field.replace(".", "_");
  }
}

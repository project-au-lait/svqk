package dev.aulait.svqk.arch.jpa;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;

import dev.aulait.svqk.arch.search.FieldConditionVo;
import dev.aulait.svqk.arch.search.OperatorCd;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import lombok.Getter;

public class SearchQueryBuilder {

  @Getter
  private Map<String, Object> queryParams = new HashMap<>();

  @Getter
  private String countQuery;

  @Getter
  private String searchQuery;

  public void buildQuery(String entityName, SearchConditionVo condition) {
    StringBuilder search = new StringBuilder();
    search.append("SELECT e FROM ").append(entityName).append(" e");

    String where = buildWhere("e", condition.getFieldConditions());
    if (where.length() > 0) {
      search.append(" WHERE ").append(where);
    }

    search.append(buildOrderBy("e", condition.getSort()));

    searchQuery = search.toString();

    StringBuilder count = new StringBuilder();
    count.append("SELECT COUNT(e) FROM ").append(entityName).append(" e");
    if (where.length() > 0) {
      count.append(" WHERE ").append(where);
    }

    countQuery = count.toString();
  }

  String buildWhere(String shortName, List<FieldConditionVo> fieldConditions) {
    StringBuilder sb = new StringBuilder();

    Iterator<FieldConditionVo> itr = fieldConditions.stream().filter(FieldConditionVo::hasValue).iterator();

    while (itr.hasNext()) {
      FieldConditionVo fieldCondition = itr.next();
      String field = fieldCondition.getField();

      sb.append(shortName + "." + field + " ");

      OperatorCd operator = fieldCondition.getOperator();

      sb.append(operator.getValue() + " :" + field);

      queryParams.put(field, fieldCondition.getValue());

      if (itr.hasNext()) {
        sb.append(" " + fieldCondition.getConjunction() + " ");
      }
    }

    return sb.toString();
  }

  String buildOrderBy(String shortName, Sort sort) {
    if (sort.isEmpty()) {
      return "";
    }

    return " ORDER BY " +
        sort.get().map(order -> shortName + "." + order.getProperty() + " " + order.getDirection())
            .collect(Collectors.joining(","));
  }
}

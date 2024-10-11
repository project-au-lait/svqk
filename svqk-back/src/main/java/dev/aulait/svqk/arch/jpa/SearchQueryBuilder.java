package dev.aulait.svqk.arch.jpa;

import dev.aulait.svqk.arch.search.FieldConditionVo;
import dev.aulait.svqk.arch.search.JoinVo;
import dev.aulait.svqk.arch.search.OperatorCd;
import dev.aulait.svqk.arch.search.SearchConditionVo;
import java.util.HashMap;
import java.util.Iterator;
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

  private String mainEntityAlias;

  public void buildQuery(SearchConditionVo condition) {
    StringBuilder search = new StringBuilder();
    String mainEntity = condition.getMainEntity().getSimpleName();
    mainEntityAlias = mainEntity.replace("Entity", "").toLowerCase();

    search
        .append("SELECT ")
        .append(mainEntityAlias)
        .append(" FROM ")
        .append(mainEntity)
        .append(" ")
        .append(mainEntityAlias);

    String join = buildJoin(condition.getJoins());
    search.append(join);

    String where = buildWhere(condition.getFieldConditions());
    if (where.length() > 0) {
      search.append(" WHERE ").append(where);
    }

    search.append(buildOrderBy(mainEntityAlias, condition.getSort()));

    searchQuery = search.toString();

    StringBuilder count = new StringBuilder();
    count
        .append("SELECT COUNT(")
        .append(mainEntityAlias)
        .append(") FROM ")
        .append(mainEntity)
        .append(" ")
        .append(mainEntityAlias)
        .append(join.replace("FETCH ", ""));
    if (where.length() > 0) {
      count.append(" WHERE ").append(where);
    }

    countQuery = count.toString();
  }

  String buildJoin(List<JoinVo> joins) {
    return joins.stream().map(this::buildJoin).collect(Collectors.joining(" "));
  }

  String buildJoin(JoinVo join) {
    StringBuilder sb = new StringBuilder();
    String fieldOwner = StringUtils.defaultIfEmpty(join.getFieldOwner(), mainEntityAlias);
    String fetch = join.isFetch() ? "FETCH " : "";

    sb.append(" JOIN ")
        .append(fetch)
        .append(fieldOwner)
        .append(".")
        .append(join.getField())
        .append(" ")
        .append(join.getAlias());

    return sb.toString();
  }

  String buildWhere(List<FieldConditionVo> fieldConditions) {
    StringBuilder sb = new StringBuilder();

    Iterator<FieldConditionVo> itr =
        fieldConditions.stream().filter(FieldConditionVo::hasValue).iterator();

    while (itr.hasNext()) {
      FieldConditionVo fieldCondition = itr.next();

      if (sb.length() > 0) {
        sb.append(" " + fieldCondition.getConjunction() + " ");
      }

      String field = fieldCondition.getField();
      String fieldOwner =
          StringUtils.defaultIfEmpty(fieldCondition.getEntityAlias(), mainEntityAlias);

      sb.append(fieldOwner + "." + field + " ");

      OperatorCd operator = fieldCondition.getOperator();

      sb.append(operator.getValue() + " :" + field);

      queryParams.put(
          field,
          operator == OperatorCd.LIKE
              ? "%" + fieldCondition.getValue() + "%"
              : fieldCondition.getValue());
    }

    return sb.toString();
  }

  String buildOrderBy(String shortName, Sort sort) {
    if (sort.isEmpty()) {
      return "";
    }

    return " ORDER BY "
        + sort.get()
            .map(order -> shortName + "." + order.getProperty() + " " + order.getDirection())
            .collect(Collectors.joining(","));
  }
}

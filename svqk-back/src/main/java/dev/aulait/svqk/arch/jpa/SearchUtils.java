package dev.aulait.svqk.arch.jpa;

import java.util.List;
import java.util.Map;

import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUtils {

  public static <T> SearchResultVo<T> search(EntityManager em, Class<T> type, SearchConditionVo condition) {

    SearchQueryBuilder builder = new SearchQueryBuilder();
    builder.buildQuery(type.getSimpleName(), condition);

    Query countQuery = em.createQuery(builder.getCountQuery());
    setQueryParams(countQuery, builder.getQueryParams());
    long count = (Long) countQuery.getSingleResult();

    if (count == 0) {
      return SearchResultVo.<T>builder().count(count).build();
    }

    Query searchQuery = em.createQuery(builder.getSearchQuery());
    setQueryParams(searchQuery, builder.getQueryParams());
    searchQuery.setMaxResults(condition.getPageSize());
    searchQuery.setFirstResult(condition.getOffset());

    @SuppressWarnings("unchecked")
    List<T> result = searchQuery.getResultList();

    return SearchResultVo.<T>builder().count(count).list(result).build();
  }

  private static void setQueryParams(Query query, Map<String, Object> params) {
    params.entrySet().stream().forEach(param -> query.setParameter(param.getKey(), param.getValue()));
  }
}

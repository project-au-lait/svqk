package dev.aulait.svqk.arch.jpa;

import dev.aulait.svqk.arch.search.SearchConditionVo;
import dev.aulait.svqk.arch.search.SearchResultVo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchUtils {

  public static <T> SearchResultVo<T> search(EntityManager em, SearchConditionVo condition) {

    SearchQueryBuilder builder = new SearchQueryBuilder();
    builder.buildQuery(condition);

    String countQueryStr = builder.getCountQuery();
    log.debug("Count query: {}", countQueryStr);

    Query countQuery = em.createQuery(countQueryStr);
    setQueryParams(countQuery, builder.getQueryParams());
    long count = (Long) countQuery.getSingleResult();

    if (count == 0) {
      return SearchResultVo.<T>builder().count(count).build();
    }

    String searchQueryStr = builder.getSearchQuery();
    log.debug("Search query: {}", searchQueryStr);

    Query searchQuery = em.createQuery(searchQueryStr);
    setQueryParams(searchQuery, builder.getQueryParams());
    searchQuery.setMaxResults(condition.getPageSize());
    searchQuery.setFirstResult(condition.getOffset());

    @SuppressWarnings("unchecked")
    List<T> result = searchQuery.getResultList();

    return SearchResultVo.<T>builder()
        .count(count)
        .list(result)
        .pageSize(condition.getPageSize())
        .pageNumber(condition.getPageNumber())
        .pageNumsRange(condition.getPageNumsRange())
        .offset(condition.getOffset())
        .build();
  }

  private static void setQueryParams(Query query, Map<String, Object> params) {
    params.entrySet().stream()
        .forEach(param -> query.setParameter(param.getKey(), param.getValue()));
  }
}

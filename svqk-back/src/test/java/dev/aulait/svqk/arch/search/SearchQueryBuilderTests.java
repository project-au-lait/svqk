package dev.aulait.svqk.arch.search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Slf4j
class SearchQueryBuilderTests {

  SearchQueryBuilder builder = new SearchQueryBuilder();

  @Nested
  class BuildWhereTests {
    @Test
    void testSingleField() {
      List<FieldCriteriaVo> criteria = List.of(FieldCriteriaVo.of("field", "1"));

      String where = builder.buildWhere(criteria);

      assertEquals("field = :field", where);
      assertEquals("1", builder.getQueryParams().get("field"));
    }

    @Test
    void testMultipleField() {
      List<FieldCriteriaVo> criteria =
          List.of(FieldCriteriaVo.of("field1", "1"), FieldCriteriaVo.of("field2", "2"));

      String where = builder.buildWhere(criteria);

      assertEquals("field1 = :field1 AND field2 = :field2", where);
      assertEquals("1", builder.getQueryParams().get("field1"));
      assertEquals("2", builder.getQueryParams().get("field2"));
    }

    @Test
    void testIn() {
      List<FieldCriteriaVo> criteria =
          List.of(FieldCriteriaVo.of("field", ComparisonOperatorCd.IN, List.of("1", "2")));

      String where = builder.buildWhere(criteria);

      assertEquals("field IN :field", where);
      assertEquals(List.of("1", "2"), builder.getQueryParams().get("field"));
    }
  }

  @Test
  void testOrderBy() {
    List<SortOrderDto> orders = List.of(SortOrderDto.builder().field("field").asc(true).build());
    String orderBy = builder.buildOrderBy(orders);

    assertEquals(" ORDER BY field ASC", orderBy);
  }

  @Nested
  class WithSearchCriteriaTests {
    @Test
    void test() {
      SearchCriteriaBuilder cBuilder = new SearchCriteriaBuilder();
      cBuilder
          .select("SELECT t FROM TestEntity t")
          .select("JOIN FETCH t.join j")
          .where("t.field", "a")
          .where("j.field", "b");

      builder.buildQuery(cBuilder.build(new PageControlDto()));

      assertEquals(
          "SELECT COUNT(t) FROM TestEntity t JOIN t.join j"
              + " WHERE t.field = :t_field AND j.field = :j_field",
          builder.getCountQuery());

      assertEquals(
          "SELECT t FROM TestEntity t JOIN FETCH t.join j"
              + " WHERE t.field = :t_field AND j.field = :j_field",
          builder.getSearchQuery());
    }
  }
}

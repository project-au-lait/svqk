package dev.aulait.svqk.arch.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.svqk.arch.search.FieldConditionVo;
import dev.aulait.svqk.arch.search.OperatorCd;
import dev.aulait.svqk.arch.search.SearchConditionBuilder;
import dev.aulait.svqk.arch.search.SearchConditionDto;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Slf4j
class SearchQueryBuilderTests {

  SearchQueryBuilder builder = new SearchQueryBuilder();

  @Nested
  class BuildWhereTests {
    @Test
    void testSingleField() {
      List<FieldConditionVo> conditions = List.of(FieldConditionVo.of("field", "1"));

      String where = builder.buildWhere(conditions);

      assertEquals("null.field = :field", where);
      assertEquals("1", builder.getQueryParams().get("field"));
    }

    @Test
    void testMultipleField() {
      List<FieldConditionVo> conditions =
          List.of(FieldConditionVo.of("field1", "1"), FieldConditionVo.of("field2", "2"));

      String where = builder.buildWhere(conditions);

      assertEquals("null.field1 = :field1 AND null.field2 = :field2", where);
      assertEquals("1", builder.getQueryParams().get("field1"));
      assertEquals("2", builder.getQueryParams().get("field2"));
    }

    @Test
    void testIn() {
      List<FieldConditionVo> conditions =
          List.of(FieldConditionVo.of("field", OperatorCd.IN, List.of("1", "2")));

      String where = builder.buildWhere(conditions);

      assertEquals("null.field IN :field", where);
      assertEquals(List.of("1", "2"), builder.getQueryParams().get("field"));
    }
  }

  @Test
  void testOrderBy() {
    Sort sort = Sort.by(Direction.ASC, "field");
    String orderBy = builder.buildOrderBy("e", sort);

    assertEquals(" ORDER BY e.field ASC", orderBy);
  }

  @Nested
  class WithSearchConcitionTests {
    @Test
    void testJoin() {
      SearchConditionBuilder cBuilder = new SearchConditionBuilder();
      cBuilder
          .from(TestEntity.class)
          .join("joinEntity")
          .where("field", "a")
          .where("joinEntity", "joinEntityField", "b");

      SearchConditionDto cond = new SearchConditionDto();

      builder.buildQuery(cBuilder.build(cond));

      assertEquals(
          "SELECT COUNT(test) FROM TestEntity test JOIN test.joinEntity joinEntity"
              + " WHERE test.field = :field AND joinEntity.joinEntityField = :joinEntityField",
          builder.getCountQuery());

      assertEquals(
          "SELECT test FROM TestEntity test JOIN FETCH test.joinEntity joinEntity"
              + " WHERE test.field = :field AND joinEntity.joinEntityField = :joinEntityField",
          builder.getSearchQuery());
    }
  }

  class TestEntity {}
}

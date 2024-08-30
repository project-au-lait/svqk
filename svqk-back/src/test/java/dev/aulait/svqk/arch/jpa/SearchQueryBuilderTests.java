package dev.aulait.svqk.arch.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import dev.aulait.svqk.arch.search.FieldConditionVo;
import dev.aulait.svqk.arch.search.OperatorCd;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class SearchQueryBuilderTests {

  SearchQueryBuilder builder = new SearchQueryBuilder();

  @Test
  void testBuildWhereSingleField() {
    List<FieldConditionVo> conditions = List.of(FieldConditionVo.of("field", "1"));

    String where = builder.buildWhere("e", conditions);

    assertEquals("e.field = :field", where);
    assertEquals("1", builder.getQueryParams().get("field"));
  }

  @Test
  void testBuildWhereMultipleField() {
    List<FieldConditionVo> conditions = List.of(FieldConditionVo.of("field1", "1"),
        FieldConditionVo.of("field2", "2"));

    String where = builder.buildWhere("e", conditions);

    assertEquals("e.field1 = :field1 AND e.field2 = :field2", where);
    assertEquals("1", builder.getQueryParams().get("field1"));
    assertEquals("2", builder.getQueryParams().get("field2"));
  }

  @Test
  void testBuildWhereIn() {
    List<FieldConditionVo> conditions = List.of(FieldConditionVo.of("field", OperatorCd.IN, List.of("1", "2")));

    String where = builder.buildWhere("e", conditions);

    assertEquals("e.field IN :field", where);
    assertEquals(List.of("1", "2"), builder.getQueryParams().get("field"));
  }

  @Test
  void testOrderBy() {
    Sort sort = Sort.by(Direction.ASC, "field");
    String orderBy = builder.buildOrderBy("e", sort);

    assertEquals(" ORDER BY e.field ASC", orderBy);
  }

}

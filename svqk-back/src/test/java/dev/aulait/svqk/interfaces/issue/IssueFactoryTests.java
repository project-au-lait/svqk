package dev.aulait.svqk.interfaces.issue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.svqk.arch.jpa.SearchQueryBuilder;
import org.junit.jupiter.api.Test;

class IssueFactoryTests {

  IssueFactory factory = new IssueFactory();

  @Test
  void testBuild() {
    IssueSearchConditionDto cond = new IssueSearchConditionDto();

    cond.setText("text");
    cond.setSubjectOnly(true);

    SearchQueryBuilder builder = new SearchQueryBuilder();
    builder.buildQuery("IssueEntity", factory.build(cond));

    assertEquals(
        "SELECT e FROM IssueEntity e WHERE e.subject LIKE :subject ORDER BY e.id DESC",
        builder.getSearchQuery());
  }
}

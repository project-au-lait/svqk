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
    builder.buildQuery(factory.build(cond));

    assertEquals(
        "SELECT IssueEntity FROM IssueEntity JOIN IssueEntity.issueStatus WHERE IssueEntity.subject"
            + " LIKE :subject ORDER BY IssueEntity.id DESC",
        builder.getSearchQuery());
  }
}

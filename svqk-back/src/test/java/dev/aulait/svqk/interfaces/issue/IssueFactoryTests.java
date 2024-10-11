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
        "SELECT issue FROM IssueEntity issue JOIN FETCH issue.issueStatus issueStatus WHERE"
            + " issue.subject LIKE :subject ORDER BY issue.id DESC",
        builder.getSearchQuery());
  }
}

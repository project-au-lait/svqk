package dev.aulait.svqk.interfaces.issue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.svqk.arch.jpa.SearchQueryBuilder;
import org.junit.jupiter.api.Test;

class IssueFactoryTests {

  IssueFactory factory = new IssueFactory();

  @Test
  void testBuild() {
    IssueSearchCriteriaDto criteria = new IssueSearchCriteriaDto();

    criteria.setText("text");
    criteria.setSubjectOnly(true);

    SearchQueryBuilder builder = new SearchQueryBuilder();
    builder.buildQuery(factory.build(criteria));

    assertEquals(
        "SELECT i FROM IssueEntity i JOIN FETCH i.issueStatus s JOIN FETCH"
            + " i.tracker t WHERE i.subject LIKE :i.subject ORDER BY i.id DESC",
        builder.getSearchQuery());
  }
}

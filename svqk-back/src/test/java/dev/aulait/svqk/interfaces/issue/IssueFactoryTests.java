package dev.aulait.svqk.interfaces.issue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.svqk.arch.jpa.SearchQueryBuilder;
import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import dev.aulait.svqk.domain.issue.IssueTrackingRs;
import dev.aulait.svqk.domain.issue.TrackerEntity;
import dev.aulait.svqk.interfaces.issue.IssueTrackingDto.IssueStatusCountDto;
import dev.aulait.svqk.interfaces.issue.IssueTrackingDto.TrackerCountDto;
import java.util.Iterator;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Nested;
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
        "SELECT issue FROM IssueEntity issue JOIN FETCH issue.issueStatus issueStatus JOIN FETCH"
            + " issue.tracker tracker WHERE issue.subject LIKE :subject ORDER BY issue.id DESC",
        builder.getSearchQuery());
  }

  @Nested
  class BuildIssueTrackingTests {

    @Test
    void testBuildIssueTrackingDto() {
      List<IssueTrackingRs> issueTrackings =
          List.of(
              issueTracking("2", "1", 1),
              issueTracking("1", "2", 10),
              issueTracking("1", "1", 100));

      IssueTrackingDto issueTracking = factory.buildIssueTracking(issueTrackings);

      Iterator<TrackerCountDto> itr = issueTracking.getTrackers().iterator();

      TrackerCountDto tracker1 = itr.next();
      assertEquals("1", tracker1.getTracker().getId());
      assertEquals(110, tracker1.getTotal());

      IssueStatusCountDto status11 = tracker1.getIssueStatusMap().get("1");
      assertEquals("1", status11.getIssueStatus().getId());
      assertEquals(100, status11.getCount());

      IssueStatusCountDto status12 = tracker1.getIssueStatusMap().get("2");
      assertEquals("2", status12.getIssueStatus().getId());
      assertEquals(10, status12.getCount());

      TrackerCountDto tracker2 = itr.next();
      assertEquals("2", tracker2.getTracker().getId());
      assertEquals(1, tracker2.getTotal());

      IssueStatusCountDto status21 = tracker2.getIssueStatusMap().get("1");
      assertEquals("1", status21.getIssueStatus().getId());
      assertEquals(1, status21.getCount());
    }

    IssueTrackingRs issueTracking(String trackerId, String issueStatusId, long count) {
      TrackerEntity tracker = new TrackerEntity();
      tracker.setId(trackerId);

      IssueStatusEntity issueStatus = new IssueStatusEntity();
      issueStatus.setId(issueStatusId);

      return IssueTrackingData.builder()
          .tracker(tracker)
          .issueStatus(issueStatus)
          .count(count)
          .build();
    }

    @Data
    @Builder
    static class IssueTrackingData implements IssueTrackingRs {
      private TrackerEntity tracker;
      private IssueStatusEntity issueStatus;
      private long count;
    }
  }
}

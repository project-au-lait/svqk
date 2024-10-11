package dev.aulait.svqk.interfaces.issue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueDataFactory {

  public static IssueDto createRandomIssue() { // <.>
    IssueDto issue = new IssueDto();
    issue.setSubject("test subject: " + RandomStringUtils.randomAlphanumeric(5));

    IssueStatusDto status = new IssueStatusDto();
    status.setId("1");
    issue.setIssueStatus(status);

    TrackerDto tracker = new TrackerDto();
    tracker.setId("1");
    issue.setTracker(tracker);

    return issue;
  }
}

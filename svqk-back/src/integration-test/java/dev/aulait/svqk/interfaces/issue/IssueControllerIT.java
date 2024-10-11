package dev.aulait.svqk.interfaces.issue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.svqk.arch.test.ConstraintViolationResponseDto;
import dev.aulait.svqk.arch.test.ValidationMessageUtils;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import dev.aulait.svqk.arch.test.ConstraintViolationResponseDto;
import dev.aulait.svqk.interfaces.tracker.TrackerDto;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest // <.>
class IssueControllerIT {

  IssueClient client = new IssueClient(); // <.>

  @Test // <.>
  void testCrud() {
    IssueDto issue = IssueDataFactory.createRandomIssue(); // <.>

    IssueStatusDto status = new IssueStatusDto();
    status.setId("1");
    issue.setIssueStatus(status);

    TrackerDto tracker = new TrackerDto();
    tracker.setId(1);
    issue.setTracker(tracker);

    // Create
    int issueId = client.save(issue).getId(); // <.>

    // Reference
    IssueDto createdIssue = client.get(issueId);
    assertEquals(issue.getSubject(), createdIssue.getSubject()); // <.>

    // Update
    createdIssue.setSubject("test subject: " + RandomStringUtils.randomAlphanumeric(5));
    client.save(createdIssue);

    IssueDto updatedIssue = client.get(issueId);

    assertEquals(createdIssue.getSubject(), updatedIssue.getSubject());
  }

  @Test
  void testCreateButValidationError() {
    IssueDto issue = new IssueDto();

    ConstraintViolationResponseDto error = client.createButValidationError(issue);

    assertEquals(
        ValidationMessageUtils.getNotBlankMsg(), error.getViolations().get(0).getMessage());
  }
}

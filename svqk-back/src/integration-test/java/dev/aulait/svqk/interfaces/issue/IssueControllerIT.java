package dev.aulait.svqk.interfaces.issue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ResourceBundle;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import dev.aulait.svqk.arch.test.ConstraintViolationResponseDto;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
class IssueControllerIT {

  IssueClient client = new IssueClient();

  @Test
  void testCrud() {
    IssueDto issue = new IssueDto();
    issue.setSubject("test subject: " + RandomStringUtils.randomAlphanumeric(5));

    IssueStatusDto status = new IssueStatusDto();
    status.setId("1");
    issue.setIssueStatus(status);

    int issueId = client.save(issue).getId();

    IssueDto createdIssue = client.get(issueId);

    createdIssue.setSubject("test subject: " + RandomStringUtils.randomAlphanumeric(5));
    client.save(createdIssue);

    IssueDto updatedIssue = client.get(issueId);

    assertEquals(createdIssue.getSubject(), updatedIssue.getSubject());
  }

  @Test
  void testCreateButValidationError() {
    IssueDto issue = new IssueDto();

    ConstraintViolationResponseDto error = client.createButValidationError(issue);

    ResourceBundle rb = ResourceBundle.getBundle("org.hibernate.validator.ValidationMessages");
    String errorMsg = rb.getString("jakarta.validation.constraints.NotBlank.message");

    assertEquals(errorMsg, error.getViolations().get(0).getMessage());
  }
}

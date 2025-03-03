package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.arch.test.RestAssuredUtils.given;
import static dev.aulait.svqk.interfaces.issue.IssueController.*;

import dev.aulait.svqk.arch.test.ConstraintViolationResponseDto;

public class IssueClient {

  public Integer create(IssueDto issue) { // <.>
    return Integer.parseInt(
        given() // <.>
            .body(issue)
            .post(ISSUES_PATH)
            .then()
            .statusCode(200)
            .extract()
            .asString());
  }

  public ConstraintViolationResponseDto createButValidationError(IssueDto issue) {
    return given()
        .body(issue)
        .post(ISSUES_PATH)
        .then()
        .statusCode(400)
        .extract()
        .as(ConstraintViolationResponseDto.class);
  }

  public IssueDto get(int issueId) {
    return given()
        .get(ISSUES_PATH + "/" + ISSUES_ID_PATH, issueId)
        .then()
        .statusCode(200)
        .extract()
        .as(IssueDto.class);
  }

  public Integer update(IssueUpdateDto issue) { // <.>
    return Integer.parseInt(
        given() // <.>
            .body(issue)
            .put(ISSUES_PATH)
            .then()
            .statusCode(200)
            .extract()
            .asString());
  }
}

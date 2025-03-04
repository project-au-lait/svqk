package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.arch.test.RestAssuredUtils.given;
import static dev.aulait.svqk.interfaces.issue.IssueController.ISSUES_ID_PATH;
import static dev.aulait.svqk.interfaces.issue.IssueController.ISSUES_PATH;

import dev.aulait.svqk.arch.test.ConstraintViolationResponseDto;
import io.restassured.response.Response;

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

  public IssueDto getOrNull(int issueId) {
    Response response = given().get(ISSUES_PATH + "/" + ISSUES_ID_PATH, issueId);

    if (response.getStatusCode() == 404) {
      return null;
    }

    return response.then().statusCode(200).extract().as(IssueDto.class);
  }

  public Integer update(IssueUpdateDto issue, int issueId) { // <.>
    return Integer.parseInt(
        given() // <.>
            .body(issue)
            .put(ISSUES_PATH + "/" + ISSUES_ID_PATH, issueId)
            .then()
            .statusCode(200)
            .extract()
            .asString());
  }

  public Integer delete(int issueId, IssueDto issueDto) {
    return Integer.parseInt(
        given()
            .body(issueDto)
            .delete(ISSUES_PATH + "/" + ISSUES_ID_PATH, issueId)
            .then()
            .statusCode(200)
            .extract()
            .asString());
  }
}

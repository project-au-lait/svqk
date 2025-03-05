package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.interfaces.issue.IssueController.ISSUES_ID_PATH;
import static dev.aulait.svqk.interfaces.issue.IssueController.ISSUES_PATH;

import dev.aulait.svqk.arch.exception.ErrorResponseDto;
import dev.aulait.svqk.arch.test.ConstraintViolationResponseDto;
import dev.aulait.svqk.arch.test.RestClientUtils;

public class IssueClient {

  private static final String CREATE_PATH = ISSUES_PATH;
  private static final String GET_PATH = ISSUES_PATH + "/" + ISSUES_ID_PATH;
  private static final String UPDATE_PATH = ISSUES_PATH + "/" + ISSUES_ID_PATH;
  private static final String DELETE_PATH = ISSUES_PATH + "/" + ISSUES_ID_PATH;

  public Integer create(IssueDto issue) { // <.>
    return RestClientUtils.post(ISSUES_PATH, issue, Integer.class);
  }

  public ConstraintViolationResponseDto createButValidationError(IssueDto issue) {
    return RestClientUtils.postWithBadRequest(CREATE_PATH, issue);
  }

  public IssueDto get(int issueId) {
    return RestClientUtils.get(GET_PATH, IssueDto.class, issueId);
  }

  public ErrorResponseDto getWithError(int issueId) {
    return RestClientUtils.getWithError(GET_PATH, issueId);
  }

  public Integer update(IssueUpdateDto issue, int issueId) { // <.>
    return RestClientUtils.put(UPDATE_PATH, issue, Integer.class, issueId);
  }

  public Integer delete(int issueId, IssueDto issue) {
    return RestClientUtils.delete(DELETE_PATH, issue, Integer.class, issueId);
  }
}

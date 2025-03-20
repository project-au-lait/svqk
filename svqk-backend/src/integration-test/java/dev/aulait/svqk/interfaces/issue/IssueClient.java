package dev.aulait.svqk.interfaces.issue;

import static dev.aulait.svqk.interfaces.issue.IssueController.ISSUES_ID_PATH;
import static dev.aulait.svqk.interfaces.issue.IssueController.ISSUES_PATH;

import dev.aulait.svqk.arch.exception.ErrorResponseDto;
import dev.aulait.svqk.arch.test.ConstraintViolationResponseDto;
import dev.aulait.svqk.arch.test.RestClientUtils;

public class IssueClient {

  private static final String ISSUES_AND_ISSUES_ID_PATH = ISSUES_PATH + "/" + ISSUES_ID_PATH;

  public int create(IssueDto issue) { // <.>
    return RestClientUtils.post(ISSUES_PATH, issue, Integer.class); // <.>
  }

  public ConstraintViolationResponseDto createButValidationError(IssueDto issue) {
    return RestClientUtils.postWithBadRequest(ISSUES_PATH, issue);
  }

  public IssueDto get(int issueId) {
    return RestClientUtils.get(ISSUES_AND_ISSUES_ID_PATH, IssueDto.class, issueId);
  }

  public ErrorResponseDto getWithError(int issueId) {
    return RestClientUtils.getWithError(ISSUES_AND_ISSUES_ID_PATH, issueId);
  }

  public int update(IssueUpdateDto issue, int issueId) { // <.>
    return RestClientUtils.put(ISSUES_AND_ISSUES_ID_PATH, issue, Integer.class, issueId);
  }

  public Integer delete(int issueId, IssueDto issue) {
    return RestClientUtils.delete(ISSUES_AND_ISSUES_ID_PATH, issue, Integer.class, issueId);
  }
}

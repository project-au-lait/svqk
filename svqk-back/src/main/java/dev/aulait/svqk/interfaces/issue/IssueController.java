package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.domain.issue.IssueEntity;
import dev.aulait.svqk.domain.issue.IssueService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Path(IssueController.ISSUE_PATH)
@RequiredArgsConstructor
public class IssueController {

  private final IssueService issueService;

  static final String ISSUE_PATH = ApiPath.ROOT + "/issue";

  static final String ISSUE_GET_PATH = "{id}";

  @GET
  @Path(ISSUE_GET_PATH)
  public IssueDto get(@PathParam("id") int id) {

    IssueEntity entity = issueService.find(id);

    return IssueDto.builder().id(entity.getId()).message(entity.getMessage()).build();
  }

  @POST
  public int save(@Valid IssueDto dto) {

    IssueEntity entity = new IssueEntity();
    entity.setId(dto.getId());
    entity.setMessage(dto.getMessage());

    IssueEntity savedEntity = issueService.save(entity);

    return savedEntity.getId();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class ApiPath {
    static final String ROOT = "/api/v1";
  }
}

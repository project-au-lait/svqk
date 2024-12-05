package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.domain.issue.IssueStatusEntity;
import dev.aulait.svqk.domain.issue.IssueStatusService;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Path(IssueStatusController.ISSUESTATUS_PATH)
@RequiredArgsConstructor
public class IssueStatusController {

  private final IssueStatusService issueStatusService;

  static final String ISSUESTATUS_PATH = ApiPath.ROOT + "/issueStatus";

  static final String ISSUESTATUS_GET_PATH = "{id}";

  @GET
  @Path(ISSUESTATUS_GET_PATH)
  public IssueStatusDto get(@PathParam("id") int id) {

    IssueStatusEntity entity = issueStatusService.find(id);

    return IssueStatusDto.builder().id(entity.getId()).message(entity.getMessage()).build();
  }

  @POST
  public int save(@Valid IssueStatusDto dto) {

    IssueStatusEntity entity = new IssueStatusEntity();
    entity.setId(dto.getId());
    entity.setMessage(dto.getMessage());

    IssueStatusEntity savedEntity = issueStatusService.save(entity);

    return savedEntity.getId();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class ApiPath {
    static final String ROOT = "/api/v1";
  }
}
